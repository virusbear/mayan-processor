package com.virusbear.beanstalkd.response

import com.virusbear.beanstalkd.readResponse
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class WatchingResponseTest {
    @Test
    fun `code is WATCHING`() {
        assertEquals("WATCHING", WatchingResponse.code)
    }

    @Test
    fun read() = runBlocking {
        val count = 263778768u
        val params = listOf("$count")
        val channel = ByteReadChannel(byteArrayOf())

        val response = WatchingResponse.readResponse<WatchingResponse>(params, channel)

        assertEquals(count, response.count)
    }
}