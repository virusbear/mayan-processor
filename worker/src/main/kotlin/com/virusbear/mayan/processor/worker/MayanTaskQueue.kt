package com.virusbear.mayan.processor.worker

interface MayanTaskQueue {
    suspend fun send(id: Int)
    operator fun iterator(): MayanTaskIterator
}