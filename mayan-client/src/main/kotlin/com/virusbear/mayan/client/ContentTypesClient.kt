package com.virusbear.mayan.client

import com.virusbear.mayan.client.model.ContentType

class ContentTypesClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun listContentTypes(): List<ContentType> =
        getPaged({
            val response = api.contentTypes.contentTypesList(page = it)

            response.results to response.next
        }) {
            ContentType(it)
        }

    suspend fun getContentType(id: Int): ContentType =
        api.contentTypes.contentTypesRead(id.toString()).let {
            ContentType(it)
        }
}