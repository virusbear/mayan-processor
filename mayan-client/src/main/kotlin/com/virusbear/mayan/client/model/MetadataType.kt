package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiMetadataType
import com.virusbear.mayan.client.MayanClient
import com.virusbear.mayan.client.MetadataTypeClient
import java.net.URI

class MetadataType(
    val client: MetadataTypeClient,
    private val api: ApiMetadataType
) {
    companion object {
        suspend fun all(client: MayanClient): List<MetadataType> =
            client.metadataTypes.listMetadataTypes()

        suspend fun get(client: MayanClient, id: Int): MetadataType =
            client.metadataTypes.getMetadataType(id)

        suspend fun create(
            client: MayanClient,
            label: String,
            name: String,
            default: String? = null,
            lookup: String? = null,
            parser: String? = null,
            parserArguments: String? = null,
            validation: String? = null,
            validationArguments: String? = null
        ): MetadataType =
            client.metadataTypes.create(
                ApiMetadataType(
                    label = label,
                    name = name,
                    default = default,
                    lookup = lookup,
                    parser = parser,
                    parserArguments = parserArguments,
                    validation = validation,
                    validationArguments = validationArguments
                )
            )
    }

    //region fields
    val label: String
        get() = api.label

    val name: String
        get() = api.name

    val default: String
        get() = api.default!!

    val id: Int
        get() = api.id!!

    val lookup: String
        get() = api.lookup!!

    val parser: String
        get() = api.parser!!

    val parserArguments: String
        get() = api.parserArguments!!

    val url: URI
        get() = api.url!!

    val validation: String
        get() = api.validation!!

    val validationArguments: String
        get() = api.validationArguments!!
    //endregion

    //region navigate_multiple
    //endregion

    //region navigate_single
    //endregion

    //region operations
    //endregion
}