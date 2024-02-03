package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiCabinet
import com.virusbear.mayan.client.CabinetClient
import com.virusbear.mayan.client.MayanClient
import java.net.URI

class Cabinet(
    private val client: CabinetClient,
    private val api: ApiCabinet
) {
    companion object {
        suspend fun all(client: MayanClient): List<Cabinet> =
            client.cabinets.listCabinets()

        suspend fun get(client: MayanClient, id: Int): Cabinet =
            client.cabinets.getCabinet(id)

        suspend fun create(client: MayanClient, label: String, parent: Int? = null, children: List<String>? = null): Cabinet =
            client.cabinets.create(
                ApiCabinet(
                    label = label,
                    parent = parent,
                    children = children?.map { ApiCabinet(label = it) }
                )
            )
    }

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
    suspend fun listDocuments(): List<Document> =
        client.listDocuments(this.id)
    //endregion

    //region navigate_single
    suspend fun addDocument(id: Int) {
        client.addDocument(this.id, id)
    }

    suspend fun removeDocument(id: Int) {
        client.removeDocument(this.id, id)
    }
    //endregion

    //region operations
    suspend fun delete() {
        client.delete(this.id)
    }

    suspend fun update(label: String = this.label, parent: Int? = this.parent, children: List<String>? = this.children.map { it.label }): Cabinet =
        client.update(
            this.id,
            api.copy(
                label = label,
                parent = parent,
                children = children?.map { ApiCabinet(label = it) }
            )
        )
    //endregion
}