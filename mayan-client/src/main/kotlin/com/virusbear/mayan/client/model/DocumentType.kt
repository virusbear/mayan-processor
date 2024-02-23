package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentType
import com.virusbear.mayan.client.DocumentTypeClient
import com.virusbear.mayan.client.MayanClient
import java.net.URI
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class DocumentType(
    private val client: DocumentTypeClient,
    private val api: ApiDocumentType
) {
    companion object {
        suspend fun all(client: MayanClient): List<DocumentType> =
            client.documentTypes.listDocumentTypes()

        suspend fun get(client: MayanClient, id: Int): DocumentType =
            client.documentTypes.getDocumentType(id)
    }

    //region fields
    val label: String
        get() = api.label

    val deleteTimePeriod: Duration
        get() = when(api.deleteTimeUnit!!) {
            ApiDocumentType.DeleteTimeUnit.Minutes -> api.deleteTimePeriod!!.minutes
            ApiDocumentType.DeleteTimeUnit.Hours -> api.deleteTimePeriod!!.hours
            ApiDocumentType.DeleteTimeUnit.Days -> api.deleteTimePeriod!!.days
        }

    val filenameGeneratorBackend: String
        get() = api.filenameGeneratorBackend!!

    val filenameGeneratorBackendArguments: String
        get() = api.filenameGeneratorBackendArguments!!

    val id: Int
        get() = api.id!!

    val quickLabelListUrl: URI
        get() = api.quickLabelListUrl!!

    val trashTimePeriod: Duration
        get() = when(api.trashTimeUnit!!) {
            ApiDocumentType.TrashTimeUnit.Minutes -> api.trashTimePeriod!!.minutes
            ApiDocumentType.TrashTimeUnit.Hours -> api.trashTimePeriod!!.hours
            ApiDocumentType.TrashTimeUnit.Days -> api.trashTimePeriod!!.days
        }

    val url: URI
        get() = api.url!!
    //endregion

    //region navigate_single
    suspend fun getOcrSettings(): OcrSettings =
        client.getOcrSettings(this.id)

    suspend fun getParsingSettings(): ParsingSettings =
        client.getParsingSettings(this.id)
    //endregion

    //region operations

    //endregion
}