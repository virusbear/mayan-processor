package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentVersionPage
import com.virusbear.mayan.client.DocumentClient
import java.net.URI

class DocumentVersionPage(
    private val client: DocumentClient,
    private val documentId: Int,
    private val api: ApiDocumentVersionPage
) {
    //region fields
    val contentTypeId: Int
        get() = api.contentTypeId

    val objectId: Int
        get() = api.objectId

    val contentType: ContentType
        get() = ContentType(api.contentType!!)

    val documentVersionId: Int
        get() = api.documentVersionId?.toInt()!!

    val documentVersionUrl: URI
        get() = api.documentVersionUrl!!

    val id: Int
        get() = api.id!!

    val imageUrl: URI
        get() = api.imageUrl!!

    val pageNumber: Int
        get() = api.pageNumber!!

    val url: URI
        get() = api.url!!
    //endregion

    //region navigate_multiple
    //endregion

    //region navigate_single
    //endregion

    //region operations
    suspend fun delete() {
        client.deleteVersionPage(documentId, documentVersionId, id)
    }

    suspend fun downloadImage(): ByteArray =
        client.downloadVersionPageImage(documentId, documentVersionId, id)

    suspend fun getContent(): String =
        client.getVersionPageContent(documentId, documentVersionId, id)
    //endregion
}