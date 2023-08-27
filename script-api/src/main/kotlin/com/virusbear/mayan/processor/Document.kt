package com.virusbear.mayan.processor

import java.time.LocalDateTime

interface Document {
    suspend fun id(): Int
    suspend fun name(): String
    suspend fun filename(): String
    suspend fun filesize(): Int
    suspend fun timestamp(): LocalDateTime
    suspend fun type(): String
    suspend fun content(): String
    suspend fun indices(): Set<String>
    suspend fun tags(): Set<String>
    suspend fun metadata(): Map<String, String>
    suspend fun pages(): List<Page>
    suspend fun mimetype(): String
    suspend fun file(): ByteArray
}