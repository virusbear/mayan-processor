package com.virusbear.mayan.client

import com.virusbear.mayan.client.model.DocumentType
import com.virusbear.mayan.client.model.OcrSettings
import com.virusbear.mayan.client.model.ParsingSettings

class DocumentTypeClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun listDocumentTypes(): List<DocumentType> =
        getPaged({
            val response = api.documentTypes.documentTypesList(page = it)

            response.results to response.next
        }) {
            DocumentType(this, it)
        }

    suspend fun getDocumentType(id: Int): DocumentType =
        DocumentType(this, api.documentTypes.documentTypesRead(id.toString()))

    suspend fun getOcrSettings(id: Int): OcrSettings =
        api.documentTypes.documentTypesOcrSettingsRead(id.toString()).let {
            OcrSettings(it)
        }

    suspend fun getParsingSettings(id: Int): ParsingSettings =
        api.documentTypes.documentTypesParsingSettingsRead(id.toString()).let {
            ParsingSettings(it)
        }
}