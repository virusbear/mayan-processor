package com.virusbear.mayan.launcher

import com.virusbear.beanstalkt.DefaultClient
import com.virusbear.mayan.client.MayanClient
import com.virusbear.mayan.config.ConfigLoader
import com.virusbear.mayan.entrypoint.EntryPoint
import com.virusbear.mayan.launcher.beanstalkd.BeanstalkdTaskQueue
import com.virusbear.mayan.launcher.config.model.LauncherConfig
import com.virusbear.mayan.launcher.config.model.Profile
import com.virusbear.mayan.processor.worker.Worker
import kotlinx.coroutines.*
import mu.KotlinLogging
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
            val client = MayanClient(config.mayan.host, config.mayan.user, config.mayan.password)

            for(profile in config.profile) {
                when(profile) {
                    Profile.Entrypoint -> {
                        logger.info { "Launching entrypoint module" }

                        val beanstalkdClient = DefaultClient(config.queue.host, config.queue.port)
                        val queue = BeanstalkdTaskQueue(beanstalkdClient, config.queue.ttr)

                        config.queue.queue?.let { beanstalkdClient.use(it) }
                        launchRestarting(name = "EntryPoint") { EntryPoint(queue, config.entrypoint) }
                    }
                    Profile.Worker -> {
                        logger.info { "Launching worker module" }

                        val beanstalkdClient = DefaultClient(config.queue.host, config.queue.port)
                        val queue = BeanstalkdTaskQueue(beanstalkdClient, config.queue.ttr)

                        config.queue.queue?.let { beanstalkdClient.watch(it) }
                        launchRestarting(name= "Worker") { Worker(queue, config.worker, client) }
                    }
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
            logger.error(ex) { "Coroutine ${coroutineContext[CoroutineName]} failed with message ${ex.message}" }
            delay(delay)
        }
    }
}