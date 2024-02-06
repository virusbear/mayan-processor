package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiSmartLink
import com.virusbear.mayan.client.SmartLinkClient
import java.net.URI

class SmartLink(
    private val client: SmartLinkClient,
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
    suspend fun listConditions(): List<SmartLinkCondition> =
        client.listConditions(this.id)

    suspend fun listDocumentTypes(): List<DocumentType> =
        client.listDocumentTypes(this.id)
    //endregion

    //region navigate_single
    suspend fun getCondition(id: Int): SmartLinkCondition =
        client.getCondition(this.id, id)
    //endregion

    //region operations
    //endregion
}