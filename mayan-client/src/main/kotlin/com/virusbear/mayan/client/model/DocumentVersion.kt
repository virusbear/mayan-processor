package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentVersion
import com.virusbear.mayan.api.client.model.ApiDocumentVersionPage
import java.net.URI
import java.time.OffsetDateTime

class DocumentVersion(
    private val api: ApiDocumentVersion
) {
    //region fields
    val active: Boolean
        get() = api.active!!

    val comment: String
        get() = api.comment!!

    val documentId: Int
        get() = api.documentId!!.toInt()

    val documentUrl: URI
        get() = api.documentUrl!!

    val exportUrl: URI
        get() = api.exportUrl!!

    val id: Int
        get() = api.id!!

    val pageListUrl: URI
        get() = api.pageListUrl!!

    val firstPage: DocumentVersionPage
        get() = DocumentVersionPage(api.pagesFirst!!)

    val timestamp: OffsetDateTime
        get() = api.timestamp!!

    val url: URI
        get() = api.url!!
    //endregion

    //region navigate_multiple
    //endregion

    //region navigate_single
    //endregion

    //region operations
    //endregion
}