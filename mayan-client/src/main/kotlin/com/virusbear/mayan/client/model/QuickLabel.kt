package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentTypeQuickLabel
import java.net.URI

class QuickLabel(
    private val api: ApiDocumentTypeQuickLabel
) {
    //region fields
    val filename: String
        get() = api.filename

    val documentTypeUrl: URI
        get() = api.documentTypeUrl!!

    val enabled: Boolean
        get() = api.enabled!!

    val id: Int
        get() = api.id!!

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