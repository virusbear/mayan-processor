package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentType
import com.virusbear.mayan.client.DocumentTypeClient
import java.net.URI
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class DocumentType(
    private val client: DocumentTypeClient,
    private val api: ApiDocumentType
) {
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

    //region navigate_multiple
    suspend fun listMetadataTypes(): List<MetadataType> =
        client.listMetadataTypes(this.id)

    suspend fun listQuickLabels(): List<QuickLabel> =
        client.listQuickLabels(this.id)
    //endregion

    //region navigate_single
    suspend fun getMetadataType(id: Int): MetadataType =
        client.getMetadataType(this.id, id)

    suspend fun getOcrSettings(): OcrSettings =
        client.getOcrSettings(this.id)

    suspend fun getParsingSettings(): ParsingSettings =
        client.getParsingSettings(this.id)

    suspend fun getQuickLabel(id: Int): QuickLabel =
        client.getQuickLabel(this.id, id)
    //endregion

    //region operations

    //endregion
}