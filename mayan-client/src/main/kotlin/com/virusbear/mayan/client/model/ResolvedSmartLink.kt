package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiResolvedSmartLink

class ResolvedSmartLink(
    private val api: ApiResolvedSmartLink
) {
    //region fields
    val documentsUrl: String
        get() = api.documentsUrl!!

    val label: String
        get() = api.label!!

    val smartLinkUrl: String
        get() = api.smartLinkUrl!!

    val url: String
        get() = api.url!!
    //endregion

    //region navigate_multiple
    //endregion

    //region navigate_single
    //endregion

    //region operations
    //endregion
}