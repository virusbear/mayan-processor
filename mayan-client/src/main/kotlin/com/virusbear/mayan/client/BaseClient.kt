package com.virusbear.mayan.client

import java.net.URI

abstract class BaseClient(
    val api: Api
) {
    protected inline fun <T, R> getPaged(
        call: (page: Int) -> Pair<List<T>, URI?>,
        map: (T) -> R
    ): List<R> {
        var page = 0
        val result = mutableListOf<R>()
        do {
            val (results, nextUrl) = call(page++)
            result += results.map(map)
        } while(nextUrl != null)

        return result
    }
}