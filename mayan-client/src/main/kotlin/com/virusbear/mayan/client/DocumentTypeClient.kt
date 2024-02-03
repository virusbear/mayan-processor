package com.virusbear.mayan.client

import com.virusbear.mayan.client.model.DocumentType

class DocumentTypeClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun getDocumentType(id: Int): DocumentType =
        DocumentType(this, api.documentTypes.documentTypesRead(id.toString()))

    suspend fun listMetadataTypes(id: Int): List<MetadataType> =
        getPaged({
            val response = api.documentTypes.documentTypesMetadataTypesList(
                documentTypeId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            MetadataType(client.metadataTypes, it)
        }

    suspend fun listQuickLabels(id: Int): List<QuickLabel> =
        getPaged({
            val response = api.documentTypes.documentTypesQuickLabelsList(
                documentTypeId = id.toString(),
                page = it
            )

            response.results to response.next
        }) {
            QuickLabel(client.quickLabels, it)
        }

    suspend fun  getMetadataType(id: Int, metadataTypeId: Int): MetadataType =
        api.documentTypes.documentTypesMetadataTypesRead(
            documentTypeId = id.toString(),
            metadataTypeId = metadataTypeId.toString()
        ).let {
            MetadataType(client.metadataTypes, it)
        }

    suspend fun getOcrSettings(id: Int): OcrSettings =
        api.documentTypes.documentTypesOcrSettingsRead(id.toString()).let {
            OcrSettings(id, it)
        }

    suspend fun getParsingSettings(id: Int): ParsingSettings =
        api.documentTypes.documentTypesParsingSettingsRead(id.toString()).let {
            ParsingSettings(id, it)
        }

    suspend fun getQuickLabel(id: Int, quickLabelId: Int): QuickLabel =
        api.documentTypes.documentTypesQuickLabelsRead(
            documentTypeId = id.toString(),
            documentTypeQuickLabelId = quickLabelId.toString()
        ).let {
            QuickLabel(client.quickLabels, it)
        }
}