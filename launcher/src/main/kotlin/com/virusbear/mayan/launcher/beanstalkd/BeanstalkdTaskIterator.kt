package com.virusbear.mayan.launcher.beanstalkd

import com.virusbear.beanstalkd.Client
import com.virusbear.beanstalkd.Job
import com.virusbear.mayan.processor.worker.MayanTask
import com.virusbear.mayan.processor.worker.MayanTaskIterator

class BeanstalkdTaskIterator(
    private val client: Client
): MayanTaskIterator {
    var next: MayanTask? = null

    override suspend fun hasNext(): Boolean {
        return try {
            val job = client.reserve()
            next = job.asMayanTask(client)
            true
        } catch(ex: Exception) {
            next = null
            false
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