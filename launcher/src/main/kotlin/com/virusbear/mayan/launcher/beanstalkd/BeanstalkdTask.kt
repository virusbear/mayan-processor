package com.virusbear.mayan.launcher.beanstalkd

import com.virusbear.beanstalkt.Client
import com.virusbear.mayan.processor.worker.MayanTask

class BeanstalkdTask(
    private val jobId: UInt,
    private val client: Client,
    override val documentId: Int,
    override val attempts: Int
) : MayanTask {
    override suspend fun ack() {
        client.delete(jobId)
    }

    override suspend fun nack() {
        client.release(jobId)
    }
}