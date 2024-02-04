package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentFile
import com.virusbear.mayan.client.DocumentClient
import java.net.URI
import java.time.OffsetDateTime

class DocumentFile(
    private val client: DocumentClient,
    private val api: ApiDocumentFile
) {
    //region fields
    val checksum: String
        get() = api.checksum!!

    val comment: String
        get() = api.comment!!

    val documentId: Int
        get() = api.documentId!!.toInt()

    val documentUrl: URI
        get() = api.documentUrl!!

    val downloadUrl: URI
        get() = api.downloadUrl!!

    val encoding: String
        get() = api.encoding!!

    val file: String
        get() = api.file!!

    val filename: String
        get() = api.filename!!

    val id: Int
        get() = api.id!!

    val mimetype: String
        get() = api.mimetype!!

    val pageListUrl: URI
        get() = api.pageListUrl!!

    val firstPage: DocumentFilePage
        get() = DocumentFilePage(client, documentId, api.pagesFirst!!)

    val size: Int
        get() = api.propertySize!!

    val timestamp: OffsetDateTime
        get() = api.timestamp!!

    val url: URI
        get() = api.url!!
    //endregion
}