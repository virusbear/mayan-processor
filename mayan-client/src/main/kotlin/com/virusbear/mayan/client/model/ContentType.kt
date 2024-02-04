package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiContentType

class ContentType(
    private val api: ApiContentType
) {
    //region fields
    val appLabel: String
        get() = api.appLabel!!

    val id: Int
        get() = api.id!!

    val model: String
        get() = api.model!!
    //endregion

    //region navigate_multiple
    //endregion

    //region navigate_single
    //endregion

    //region operations
    //endregion
}