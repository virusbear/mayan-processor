package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiTag
import com.virusbear.mayan.client.MayanClient
import com.virusbear.mayan.client.TagClient
import java.net.URI

class Tag(
    private val client: TagClient,
    private val api: ApiTag
) {
    companion object {
        suspend fun all(client: MayanClient): List<Tag> =
            client.tags.listTags()

        suspend fun get(client: MayanClient, id: Int): Tag =
            client.tags.getTag(id)

        suspend fun create(client: MayanClient, color: String, label: String): Tag =
            client.tags.create(ApiTag(color, label, null, null, null))
    }

    //region fields
    val color: String
        get() = api.color

    val label: String
        get() = api.label

    val documentsUrl: URI
        get() = api.documentsUrl!!

    val id: Int
        get() = api.id!!

    val url: URI
        get() = api.url!!
    //endregion

    //region navigate_multiple
    suspend fun listDocuments(): List<Document> =
        client.listDocuments(this.id)
    //endregion

    //region navigate_single
    //endregion

    //region operations
    suspend fun delete() {
        client.delete(this.id)
    }

    suspend fun update(color: String = this.color, label: String = this.label): Tag =
        client.update(this.id, api.copy(color = color, label = label))
    //endregion
}