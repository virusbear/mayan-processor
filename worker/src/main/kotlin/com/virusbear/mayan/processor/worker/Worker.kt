package com.virusbear.mayan.processor.worker

import com.virusbear.mayan.client.MayanClient
import com.virusbear.mayan.processor.impl.MayanApiProcessingContext
import com.virusbear.mayan.processor.scripting.MayanProcessorHost
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import mu.KotlinLogging
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend fun Worker(
    queue: MayanTaskQueue,
    config: WorkerConfig,
    client: MayanClient,
    context: CoroutineContext = EmptyCoroutineContext
) {
    withContext(context + CoroutineName("Worker")) {
        val logger = KotlinLogging.logger("Worker")
        val host = MayanProcessorHost(config.scriptPath, config.watch).apply { config.disabledModules.forEach(this::disable) }

        val channel = Channel<Deferred<Unit>>(config.parallelism)

        val awaiter = launch {
            for(task in channel) {
                task.await()
            }
        }

        val scheduler = launch {
            for(task in queue) {
                if(task.attempt >= config.maxAttempts) {
                    logger.warn { "Document ${task.documentId} reached retry limit of ${config.maxAttempts}. Ignoring" }
                    continue
                }

                val deferred = async {
                    try {
                        host.process(MayanApiProcessingContext(client, client.document(task.documentId)))
                    } catch(ex: Exception) {
                        logger.error(ex) { "Error processing document ${task.documentId}" }
                        logger.info { "Requeuing document ${task.documentId}" }
                        queue.send(task.copy(attempt = task.attempt + 1))
                    }
                }

                channel.send(deferred)
            }
        }

        scheduler.join()
        awaiter.join()
    }
}

fun newDynamicThreadPool(minPoolSize: Int, maxPoolSize: Int): ExecutorService =
    ThreadPoolExecutor(
        minPoolSize.coerceAtLeast(0),
        maxPoolSize.coerceAtLeast(minPoolSize),
        60L, TimeUnit.SECONDS,
        LinkedBlockingQueue()
    )