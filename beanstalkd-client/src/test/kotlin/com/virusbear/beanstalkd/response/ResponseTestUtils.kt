package com.virusbear.beanstalkd.response

import io.ktor.utils.io.*
import kotlin.test.assertIs

internal suspend inline fun <reified T: Response> ResponseType.readResponse(params: List<String>, channel: ByteReadChannel): T {
    val response = read(params, channel)

    assertIs<T>(response)

    return response
}