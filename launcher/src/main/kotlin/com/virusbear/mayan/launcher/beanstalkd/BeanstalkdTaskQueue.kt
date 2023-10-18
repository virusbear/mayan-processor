package com.virusbear.mayan.launcher.beanstalkd

import com.virusbear.beanstalkt.Client
import com.virusbear.mayan.processor.worker.MayanTaskIterator
import com.virusbear.mayan.processor.worker.MayanTaskQueue
import java.nio.ByteBuffer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class BeanstalkdTaskQueue(
    private val client: Client,
    private val ttr: Duration = 1.0.minutes
): MayanTaskQueue {
    override suspend fun send(id: Int) {
        client.put(
            ByteBuffer.allocate(4).putInt(id).array(),
            ttr = ttr
        )
    }

    override fun iterator(): MayanTaskIterator =
        BeanstalkdTaskIterator(client)
}

