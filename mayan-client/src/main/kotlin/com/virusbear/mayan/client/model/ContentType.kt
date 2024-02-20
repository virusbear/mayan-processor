package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiContentType
import com.virusbear.mayan.client.MayanClient

class ContentType(
    private val api: ApiContentType
) {
    companion object {
        suspend fun all(client: MayanClient): List<ContentType> =
            client.contentTypes.listContentTypes()

        suspend fun get(client: MayanClient, id: Int): ContentType =
            client.contentTypes.getContentType(id)
    }

    //region fields
    val appLabel: String
        get() = api.appLabel!!

    val id: Int
        get() = api.id!!

    val model: String
        get() = api.model!!
    //endregion

    //region navigate_multiple
    //endregion

    //region navigate_single
    //endregion

    //region operations
    //endregion
}