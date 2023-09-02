package com.virusbear.beanstalkd.response

import io.ktor.utils.io.*

class UsingResponse(
    val tube: String
): Response {
    companion object: ResponseType {
        override val code: String = "USING"

        override suspend fun read(params: List<String>, channel: ByteReadChannel): Response =
            UsingResponse(params[0])
    }
}