package com.virusbear.mayan.client

import com.virusbear.mayan.api.client.model.ApiMetadataType
import com.virusbear.mayan.client.model.MetadataType

class MetadataTypeClient(
    val client: MayanClient,
    api: Api
): BaseClient(api) {
    suspend fun listMetadataTypes(): List<MetadataType> =
        getPaged({
            val response = api.metadataTypes.metadataTypesList(page = it)

            response.results to response.next
        }) {
            MetadataType(this, it)
        }

    suspend fun getMetadataType(id: Int): MetadataType =
        api.metadataTypes.metadataTypesRead(id.toString()).let {
            MetadataType(this, it)
        }

    internal suspend fun create(model: ApiMetadataType): MetadataType =
        api.metadataTypes.metadataTypesCreate(model).let {
            MetadataType(this, it)
        }
}