package com.virusbear.mayan.processor

interface ProcessingContext {
    val document: Document

    suspend fun regex(pattern: String, group: Int = 1): String
    suspend fun regex(pattern: String, group: String): String

    suspend fun tag(tag: String)
    suspend fun metadata(type: String, value: String)
    suspend fun documentType(type: String)
    suspend fun cabinet(index: String)
}