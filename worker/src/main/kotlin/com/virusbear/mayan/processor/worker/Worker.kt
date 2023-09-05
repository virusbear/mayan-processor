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

        for(task in queue) {
            if (task.attempts >= config.maxAttempts) {
                logger.warn { "Document ${task.documentId} reached retry limit of ${config.maxAttempts}. Ignoring" }
                task.ack()
                continue
            }

            try {
                host.process(MayanApiProcessingContext(client, client.document(task.documentId)))
                task.ack()
            } catch (ex: Exception) {
                logger.error(ex) { "Error processing document ${task.documentId}" }
                logger.info { "Requeuing document ${task.documentId}" }
                task.nack()
            }
        }
    }
}

fun newDynamicThreadPool(minPoolSize: Int, maxPoolSize: Int): ExecutorService =
    ThreadPoolExecutor(
        minPoolSize.coerceAtLeast(0),
        maxPoolSize.coerceAtLeast(minPoolSize),
        60L, TimeUnit.SECONDS,
        LinkedBlockingQueue()
    )