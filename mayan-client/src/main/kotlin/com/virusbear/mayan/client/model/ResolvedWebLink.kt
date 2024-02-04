package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiResolvedWebLink

class ResolvedWebLink(
    private val api: ApiResolvedWebLink
) {
    //region fields
    val id: Int
        get() = api.id!!

    val navigationUrl: String
        get() = api.navigationUrl!!

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