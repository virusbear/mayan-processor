package com.virusbear.mayan.processor

import java.time.LocalDateTime

interface Document {
    val id: Int
    val name: String
    val filename: String
    val filesize: Int
    val timestamp: LocalDateTime
    val type: String
    val content: String
    val indices: Set<String>
    val tag: Set<String>
    val metadata: Map<String, String>
    val pages: List<Page>
    val mimetype: String
    val file: ByteArray
}