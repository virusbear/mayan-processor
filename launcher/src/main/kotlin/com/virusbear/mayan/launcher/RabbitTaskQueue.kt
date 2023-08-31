package com.virusbear.mayan.launcher

import com.rabbitmq.client.*
import com.rabbitmq.client.Channel
import com.virusbear.mayan.processor.worker.BasicMayanTask
import com.virusbear.mayan.processor.worker.MayanTask
import com.virusbear.mayan.processor.worker.MayanTaskIterator
import com.virusbear.mayan.processor.worker.MayanTaskQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.*
import java.nio.ByteBuffer

class RabbitTaskQueue(
    val channel: Channel,
    val queue: String,
    val scope: CoroutineScope
): MayanTaskQueue {
    override suspend fun send(task: MayanTask) {
        channel.basicPublish("", queue, null, ByteBuffer.allocate(4).putInt(task.documentId).array())
    }

    override fun iterator(): MayanTaskIterator =
        RabbitMqConsumerIterator(scope.consumeAsChannel(channel, queue), channel)
}

class RabbitMqMayanTask(
    documentId: Int,
    attempts: Int,
    val deliveryTag: Long,
    val channel: Channel
): BasicMayanTask(documentId, attempts) {
    override suspend fun ack() {
        super.ack()
        channel.basicAck(deliveryTag, false)
    }

    override suspend fun nack() {
        super.nack()
        channel.basicReject(deliveryTag, true)
    }
}

fun CoroutineScope.consumeAsChannel(channel: Channel, queue: String): ReceiveChannel<Delivery> =
    produce {
        val tag = channel.basicConsume(queue, false,
            { _: String, delivery: Delivery ->
                trySendBlocking(delivery)
            },
            CancelCallback {  }
        )

        awaitClose {
            channel.basicCancel(tag)
        }
    }

class RabbitMqConsumerIterator(
    channel: ReceiveChannel<Delivery>,
    private val queue: Channel
): MayanTaskIterator {
    val iterator = channel.iterator()

    override suspend fun hasNext(): Boolean =
        iterator.hasNext()

    override suspend fun next(): MayanTask =
        iterator.next().let {
            val body = ByteBuffer.wrap(it.body)
            RabbitMqMayanTask(body.getInt(), it.properties.headers["x-delivery-count"].toString().toIntOrNull() ?: 0, it.envelope.deliveryTag, queue)
        }
}