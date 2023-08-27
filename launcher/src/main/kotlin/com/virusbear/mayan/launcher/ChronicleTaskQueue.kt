package com.virusbear.mayan.launcher

import com.squareup.tape.QueueFile
import com.squareup.tape.TaskQueue
import com.squareup.tape2.ObjectQueue
import com.squareup.tape2.QueueFile
import com.virusbear.mayan.processor.worker.MayanTask
import com.virusbear.mayan.processor.worker.MayanTaskIterator
import com.virusbear.mayan.processor.worker.MayanTaskQueue
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder
import org.mapdb.DBMaker
import java.io.File

class ChronicleTaskQueue(
    val queuePath: File = File("./tasks")
): MayanTaskQueue {
    private val queue = SingleChronicleQueueBuilder.single(queuePath).build()
    private val appender = queue.acquireAppender()
    private val tailer = queue.createTailer().

    override suspend fun send(task: MayanTask) {
        appender.writeMap(mapOf("documentId" to task.documentId, "attempt" to task.attempt))
    }

    override fun iterator(): MayanTaskIterator {
        tailer.
    }
}

class MapDbTaskQueue(
    val queueFile: File = File("./tasks.queue")
): MayanTaskQueue {
    val queue = QueueFile.Builder(queueFile).build()

    override suspend fun send(task: MayanTask) {
        queue.add("${task.documentId}:${task.attempt}".toByteArray())
    }

    override fun iterator(): MayanTaskIterator =
        MapDbTaskQueueIterator()

    inner class MapDbTaskQueueIterator: MayanTaskIterator {
        override suspend fun hasNext(): Boolean {
            queue.
        }

        override suspend fun next(): MayanTask {
            TODO("Not yet implemented")
        }
    }
}