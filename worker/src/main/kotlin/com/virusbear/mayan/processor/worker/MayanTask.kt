package com.virusbear.mayan.processor.worker

interface MayanTask {
    val documentId: Int
    val attempts: Int
    suspend fun ack()
    suspend fun nack()
}