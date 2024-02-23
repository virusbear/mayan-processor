package com.virusbear.mayan.processor

interface Page {
    val index: Int
    suspend fun content(): String
    suspend fun image(): ByteArray
}