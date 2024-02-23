package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentMetadata
import com.virusbear.mayan.client.MayanClient

class DocumentMetadata(
    private val client: MayanClient,
    private val api: ApiDocumentMetadata
) {
    //region fields
    val document: Document
        get() = Document(client.documents, api.document!!)

    val id: Int
        get() = api.id!!

    val metadataType: MetadataType
        get() = MetadataType(client.metadataTypes, api.metadataType!!)

    val url: String
        get() = api.url!!

    val value: String
        get() = api.value!!
    //endregion

    //region navigate_multiple
    //endregion

    //region navigate_single
    //endregion

    //region operations
    suspend fun delete() {
        client.documents.deleteMetadata(document.id, id)
    }

    suspend fun setValue(value: String) {
        client.documents.setMetadataValue(document.id, id, value)
    }
    //endregion
}