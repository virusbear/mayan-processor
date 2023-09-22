package com.virusbear.mayan.launcher.beanstalkd

import com.virusbear.beanstalkt.Client
import com.virusbear.beanstalkt.Job
import com.virusbear.beanstalkt.TimedOutException
import com.virusbear.mayan.processor.worker.MayanTask
import com.virusbear.mayan.processor.worker.MayanTaskIterator
import kotlin.time.Duration.Companion.seconds

class BeanstalkdTaskIterator(
    private val client: Client
): MayanTaskIterator {
    var next: MayanTask? = null

    override suspend fun hasNext(): Boolean {
        while(true) {
            return try {
                val job = client.reserveWithTimeout(10.0.seconds)
                next = job.asMayanTask(client)
                true
            } catch(ex: TimedOutException) {
                continue
            } catch(ex: Exception) {
                next = null
                false
            }
        }
    }

    override suspend fun next(): MayanTask =
        next ?: error("No next available for iterator")

    private suspend fun Job.asMayanTask(client: Client): MayanTask =
        BeanstalkdTask(
            id,
            client,
            data.position(0).getInt(),
            client.statsJob(id)["releases"]?.toInt() ?: 0
        )
}