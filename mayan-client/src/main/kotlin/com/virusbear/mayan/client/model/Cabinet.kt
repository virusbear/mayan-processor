package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiCabinet
import com.virusbear.mayan.client.CabinetClient
import java.net.URI

class Cabinet(
    private val client: CabinetClient,
    private val api: ApiCabinet
) {
    //region fields
    val children: List<Cabinet>
        get() = api.children?.map {
            Cabinet(client, it)
        } ?: emptyList()

    val documentsUrl: URI
        get() = api.documentsUrl!!

    val fullPath: String
        get() = api.fullPath!!

    val id: Int
        get() = api.id!!

    val label: String
        get() = api.label!!

    val parentId: String?
        get() = api.parentId

    val parent: Int?
        get() = api.parent

    val parentUrl: URI?
        get() = api.parentUrl?.let { URI(it) }

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