package com.virusbear.mayan.processor.impl

import MayanProcessorHost
import com.virusbear.mayan.client.MayanClient
import com.virusbear.mayan.client.model.Document as ApiDocument
import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.Page
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.runBlocking
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MayanDocument(
    val client: MayanClient,
    val document: ApiDocument
): Document {
    override val id: Int
        get() = document.id
    override val name: String
        get() = document.label ?: ""
    override val filename: String
        get() = document.fileLatest?.filename ?: ""
    override val filesize: Int
        get() = document.fileLatest?.propertySize ?: -1
    override val timestamp: LocalDateTime
        get() = document.datetimeCreated?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) } ?: LocalDateTime.MIN
    override val type: String
        get() = document.documentType?.label ?: ""
    override val content: String
        get() = pages.joinToString("\n") { it.content }
    override val indices: Set<String> by lazy {
        runBlocking {
            client.indices(document.id).map { it.value }.toSet()
        }
    }
    override val tag: Set<String> by lazy {
        runBlocking {
            client.tags(document.id).map { it.label }.toSet()
        }
    }
    override val metadata: Map<String, String> by lazy {
        runBlocking {
            client.metadata(document.id).filter { it.value != null }.map { it.type.label to it.value }.toList().toMap() as Map<String, String>
        }
    }
    override val pages: List<Page> by lazy {
        runBlocking {
            client.pages(document.id).map {
                MayanDocumentPage(client, document.id, it)
            }.toList().sortedBy { it.pageNumber }
        }
    }
    override val mimetype: String
        get() = document.fileLatest?.mimetype ?: ""
    override val file: ByteArray by lazy {
        runBlocking {
            client.fileContent(document.id, document.fileLatest?.id!!)
        }
    }
}