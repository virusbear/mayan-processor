package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentFilePage
import com.virusbear.mayan.client.DocumentClient
import java.net.URI

class DocumentFilePage(
    private val client: DocumentClient,
    val documentId: Int,
    private val api: ApiDocumentFilePage
) {
    //region fields
    val documentFileId: Int
        get() = api.documentFileId!!.toInt()

    val documentFileUrl: URI
        get() = api.documentFileUrl!!

    val id: Int
        get() = api.id!!

    val imageUrl: URI
        get() = api.imageUrl!!

    val pageNumber: Int
        get() = api.pageNumber!!

    val url: URI
        get() = api.url!!
    //endregion
}