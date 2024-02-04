package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiWebLink
import java.net.URI

class WebLink(
    private val api: ApiWebLink
) {
    //region fields
    val label: String
        get() = api.label

    val template: String
        get() = api.template

    val documentTypesAddUrl: URI
        get() = api.documentTypesAddUrl!!

    val documentTypesRemoveUrl: URI
        get() = api.documentTypesRemoveUrl!!

    val documentTypesUrl: URI
        get() = api.documentTypesUrl!!

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