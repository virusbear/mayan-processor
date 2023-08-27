package com.virusbear.mayan.launcher

import com.virusbear.mayan.client.MayanClient
import com.virusbear.mayan.config.ConfigLoader
import com.virusbear.mayan.entrypoint.EntryPoint
import com.virusbear.mayan.launcher.config.model.LauncherConfig
import com.virusbear.mayan.launcher.config.model.Profile
import com.virusbear.mayan.processor.worker.MayanTask
import com.virusbear.mayan.processor.worker.MayanTaskIterator
import com.virusbear.mayan.processor.worker.MayanTaskQueue
import com.virusbear.mayan.processor.worker.Worker
import kotlinx.coroutines.*
import mu.KotlinLogging
import mu.withLoggingContext
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

suspend fun main(args: Array<String>) =
    Launcher.run(args)

internal object Launcher {
    private val logger = KotlinLogging.logger { }

    suspend fun run(args: Array<String>) {
        val config = readConfig(args)

        supervisorScope {
            //TODO: Get dependencies
            val client = MayanClient(config.mayan.host, config.mayan.user, config.mayan.password)

            val queue = if(config.useLocalTaskQueue) {
                ChannelBackedMayanTaskQueue()
            } else {

            }

            val queue = object: MayanTaskQueue {
                override suspend fun send(task: MayanTask) {
                    println(task)
                }

                override fun iterator(): MayanTaskIterator {
                    TODO("Not yet implemented")
                }
            }

            for(profile in config.profile) {
                when(profile) {
                    Profile.Entrypoint -> launchRestarting(name = "EntryPoint") { EntryPoint(queue, config.entrypoint) }
                    Profile.Worker -> launchRestarting(name= "Worker") { Worker(queue, config.worker, client) }
                    //Profile.Ocr -> TODO("Not yet implemented!")
                    else -> TODO()
                }
            }
        }
    }

    private fun readConfig(args: Array<String>): LauncherConfig {
        logger.info { "Loading configuration" }

        val configPath = args.associate {
            val (key, value) = it.removePrefix("--").split("=")

            key to value
        }["config"]

        val configLoader = if(configPath != null) {
            logger.info { "Using config file $configPath" }
            ConfigLoader(args, File(configPath))
        } else {
            ConfigLoader(args)
        }

        return configLoader.load<LauncherConfig>().getOrThrow()
    }
}

fun CoroutineScope.launchRestarting(
    delay: Duration = 5.seconds,
    name: String = "launchRestarting",
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = launch(context, start) {
    val logger = KotlinLogging.logger(name)
    while (isActive) {
        try {
            block()
        } catch (ex: Exception) {
            logger.error(ex) { }
            delay(delay)
        }
    }
}