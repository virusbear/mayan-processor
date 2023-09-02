package com.virusbear.beanstalkd.response

import io.ktor.utils.io.*

data class InsertedResponse(
    val id: UInt
): Response {
    companion object: ResponseType {
        override val code: String = "INSERTED"

        override suspend fun read(params: List<String>, channel: ByteReadChannel): Response =
            InsertedResponse(params.first().toUInt())
    }
}