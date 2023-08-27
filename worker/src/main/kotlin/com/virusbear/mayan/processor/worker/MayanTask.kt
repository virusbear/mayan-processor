package com.virusbear.mayan.processor.worker

data class MayanTask(
    val documentId: Int,
    val attempt: Int = 0
)