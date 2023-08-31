package com.virusbear.mayan.processor.worker

open class BasicMayanTask(
    override val documentId: Int,
    override val attempts: Int = 0
): MayanTask {
    override suspend fun ack() { }
    override suspend fun nack() { }
}