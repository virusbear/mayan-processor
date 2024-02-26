package com.virusbear.mayan.processor

interface DocumentContentProvider {
    suspend fun regex(pattern: String, group: Int = 1): String
    suspend fun regex(pattern: String, group: String): String
    suspend fun ocr(region: Region): String
    fun withDocument(doc: Document)
}