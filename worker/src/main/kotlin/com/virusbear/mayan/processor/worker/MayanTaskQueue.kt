package com.virusbear.mayan.processor.worker

interface MayanTaskQueue {
    suspend fun send(task: MayanTask)
    operator fun iterator(): MayanTaskIterator
}