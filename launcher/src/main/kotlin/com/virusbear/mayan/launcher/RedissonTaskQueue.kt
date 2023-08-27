package com.virusbear.mayan.launcher

import com.virusbear.mayan.processor.worker.MayanTask
import com.virusbear.mayan.processor.worker.MayanTaskIterator
import com.virusbear.mayan.processor.worker.MayanTaskQueue

class RedissonTaskQueue: MayanTaskQueue {
    override suspend fun send(task: MayanTask) {
        TODO("Not yet implemented")
    }

    override fun iterator(): MayanTaskIterator {
        TODO("Not yet implemented")
    }
}