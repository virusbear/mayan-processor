package com.virusbear.mayan.client.clients

import com.virusbear.mayan.client.model.Document
import io.ktor.client.*
import io.ktor.http.content.*
import kotlinx.coroutines.flow.Flow

class DocumentClient(
    private val client: HttpClient
) {
    /*suspend fun downloadFile(documentId: Int, fileId: Int): ByteArray
    suspend fun filePageContent(documentId: Int, fileId: Int, pageId: Int): String
    suspend fun filePageImage(documentId: Int, fileId: Int, pageId: Int): ByteArray
    suspend fun filePageList(documentId: Int, fileId: Int): Flow<Page>
    suspend fun filePage(documentId: Int, fileId: Int, pageId: Int): Page
    suspend fun file(documentId: Int, fileId: Int): File
    suspend fun fileList(documentId: Int): Flow<File>
    suspend fun indexList(documentId: Int): Flow<Index>
    suspend fun metadataList(documentId: Int): Flow<Metadata>
    suspend fun metadata(documentId: Int, metadataId: Int): Metadata
    suspend fun tagList(documentId: Int): Flow<Tag>
    suspend fun versionList(documentId: Int): Flow<Version>
    suspend fun versionPageImage(documentId: Int, versionId: Int, pageId: Int): ByteArray
    suspend fun versionPageList(documentId: Int, versionId: Int): Flow<Page>
    suspend fun versionPageOcrContent(documentId: Int, versionId: Int, pageId: Int): String
    suspend fun versionPage(documentId: Int, versionId: Int, pageId: Int): Page
    suspend fun version(documentId: Int, versionId: Int): Version*/
}