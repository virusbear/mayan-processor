package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentTypeParsingSettings

class ParsingSettings(
    private val api: ApiDocumentTypeParsingSettings
) {
    //region fields
    val autoParsing: Boolean
        get() = api.autoParsing!!
    //endregion

    //region navigate_multiple
    //endregion

    //region navigate_single
    //endregion

    //region operations
    //endregion
}