package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiMetadataType
import java.net.URI

class MetadataType(
    private val api: ApiMetadataType
) {
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