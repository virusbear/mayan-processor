package com.virusbear.beanstalkd.response

import io.ktor.utils.io.*

data class KickedResponse(
    val count: UInt
): Response {
    companion object: ResponseType {
        override val code: String = "KICKED"

        override suspend fun read(params: List<String>, channel: ByteReadChannel): Response =
            KickedResponse(params.firstOrNull()?.toUInt() ?: 0u)
    }
}