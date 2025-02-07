package com.virusbear.mayan.processor.worker

import com.virusbear.mayan.client.MayanApi
import com.virusbear.mayan.processor.impl.MayanApiProcessingContext
import com.virusbear.mayan.processor.ocr.TesseractDocumentContentProvider
import com.virusbear.mayan.processor.scripting.MayanProcessorHost
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import mu.KotlinLogging
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend fun Worker(
    queue: MayanTaskQueue,
    config: WorkerConfig,
    client: MayanApi,
    context: CoroutineContext = EmptyCoroutineContext
) {
    withContext(context + CoroutineName("Worker")) {
        val logger = KotlinLogging.logger("Worker")
        val host = MayanProcessorHost(config.scriptPath, config.libraryPath, config.watch).apply { config.disabledModules.forEach(this::disable) }

        val dispatcher = newDynamicThreadPool(1, config.workerThreads).asCoroutineDispatcher()
        val channel = Channel<Deferred<Unit>>(config.parallelism - 1)

        val awaiter = launch {
            for(task in channel) {
                task.await()
            }
        }

        val scheduler = launch {
            for(task in queue) {
                val deferred = async(dispatcher) {
                    if (task.attempts >= config.maxAttempts) {
                        logger.warn { "Document ${task.documentId} reached retry limit of ${config.maxAttempts}. Ignoring" }
                        task.ack()
                        return@async
                    }

                    try {
                        val document = client.documents.get(task.documentId)
                        val contentProvider = TesseractDocumentContentProvider(config.tesseractDataPath)
                        host.process(MayanApiProcessingContext(client, document, contentProvider))
                        task.ack()
                    } catch (ex: Exception) {
                        logger.error(ex) { "Error processing document ${task.documentId}" }
                        logger.info { "Requeuing document ${task.documentId}" }
                        task.nack()
                    }
                }

                channel.send(deferred)
            }
        }

        scheduler.join()
        awaiter.join()
        host.close()
    }
}

internal fun newDynamicThreadPool(minPoolSize: Int, maxPoolSize: Int): ExecutorService =
    ThreadPoolExecutor(
        minPoolSize.coerceAtLeast(0),
        maxPoolSize.coerceAtLeast(minPoolSize),
        60L, TimeUnit.SECONDS,
        LinkedBlockingQueue()
    )