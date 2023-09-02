package com.virusbear.beanstalkd.response

import io.ktor.utils.io.*

interface ResponseType {
    val code: String
    suspend fun read(params: List<String>, channel: ByteReadChannel): Response
}