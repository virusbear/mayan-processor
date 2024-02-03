package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.model.ApiCabinet
import com.virusbear.mayan.api.client.model.ApiCabinetDocumentAdd
import com.virusbear.mayan.api.client.model.ApiCabinetDocumentRemove
import com.virusbear.mayan.client.model.Cabinet
import com.virusbear.mayan.client.model.Document

class CabinetClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun listCabinets(): List<Cabinet> =
        getPaged({
            val response = api.cabinets.cabinetsList(page = it)

            response.results to response.next
        }) {
            Cabinet(this, it)
        }

    suspend fun getCabinet(id: Int): Cabinet =
        api.cabinets.cabinetsRead(id.toString()).let {
            Cabinet(this, it)
        }

    internal suspend fun create(model: ApiCabinet): Cabinet =
        api.cabinets.cabinetsCreate(model).let {
            Cabinet(this, it)
        }

    internal suspend fun update(id: Int, model: ApiCabinet): Cabinet =
        api.cabinets.cabinetsUpdate(id.toString(), model).let {
            Cabinet(this, it)
        }

    suspend fun delete(id: Int) {
        api.cabinets.cabinetsDelete(id.toString())
    }

    suspend fun listDocuments(id: Int): List<Document> =
        getPaged({
            val response = api.cabinets.cabinetsDocumentsList(
                cabinetId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            Document(client.documents, it)
        }

    suspend fun addDocument(id: Int, documentId: Int) {
        api.cabinets.cabinetsDocumentsAddCreate(
            cabinetId = id.toString(),
            data = ApiCabinetDocumentAdd(documentId.toString())
        )
    }

    suspend fun removeDocument(id: Int, documentId: Int) {
        api.cabinets.cabinetsDocumentsRemoveCreate(
            cabinetId = id.toString(),
            data = ApiCabinetDocumentRemove(documentId.toString())
        )
    }
}