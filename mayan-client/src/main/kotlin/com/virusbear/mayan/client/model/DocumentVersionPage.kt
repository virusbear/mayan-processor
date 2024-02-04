package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentVersionPage
import java.net.URI

class DocumentVersionPage(
    private val api: ApiDocumentVersionPage
) {
    //region fields
    val contentTypeId: Int
        get() = api.contentTypeId

    val objectId: Int
        get() = api.objectId

    val contentType: ContentType
        get() = ContentType(api.contentType!!)

    val documentVersionId: String
        get() = api.documentVersionId!!

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
    //endregion
}