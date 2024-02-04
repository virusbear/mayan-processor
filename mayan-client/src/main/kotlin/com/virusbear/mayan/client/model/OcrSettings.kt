package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentTypeOCRSettings

class OcrSettings(
    private val api: ApiDocumentTypeOCRSettings
) {
    //region fields
    val autoOcr: Boolean
        get() = api.autoOcr!!
    //endregion

    //region navigate_multiple
    //endregion

    //region navigate_single
    //endregion

    //region operations
    //endregion
}