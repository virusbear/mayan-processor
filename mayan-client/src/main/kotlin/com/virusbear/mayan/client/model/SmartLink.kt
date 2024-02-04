package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiSmartLink
import java.net.URI

class SmartLink(
    private val api: ApiSmartLink
) {
    //region fields
    val label: String
        get() = api.label

    val conditionsUrl: URI
        get() = api.conditionsUrl!!

    val documentTypesUrl: URI
        get() = api.documentTypesUrl!!

    val documentTypesAddUrl: URI
        get() = api.documentTypesAddUrl!!

    val documentTypesRemoveUrl: URI
        get() = api.documentTypesRemoveUrl!!

    val dynamicLabel: String
        get() = api.dynamicLabel!!

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