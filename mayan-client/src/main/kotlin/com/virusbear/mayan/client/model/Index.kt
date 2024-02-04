package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiIndexInstanceNode

class Index(
    private val api: ApiIndexInstanceNode
) {
    //region fields
    val documentsUrl: String
        get() = api.documentsUrl!!

    val childrenUrl: String
        get() = api.childrenUrl!!

    val id: Int
        get() = api.id!!

    val indexUrl: String
        get() = api.indexUrl!!

    val level: Int
        get() = api.level!!

    val parentId: String
        get() = api.parentId!!

    val parentUrl: String
        get() = api.parentUrl!!

    val value: String
        get() = api.value!!

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