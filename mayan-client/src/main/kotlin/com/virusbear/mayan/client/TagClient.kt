package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.model.ApiTag
import com.virusbear.mayan.client.model.*

class TagClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun getTag(id: Int): Tag =
        Tag(this, api.tags.tagsRead(id.toString()))

    suspend fun listDocuments(id: Int): List<Document> =
        getPaged({
            val response = api.tags.tagsDocumentsList(
                tagId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            Document(client.documents, it)
        }

    suspend fun delete(id: Int) {
        api.tags.tagsDelete(id.toString())
    }

    internal suspend fun update(id: Int, model: ApiTag): Tag =
        Tag(this, api.tags.tagsUpdate(id.toString(), model))

    internal suspend fun create(model: ApiTag): Tag =
        Tag(this, api.tags.tagsCreate(model))
}