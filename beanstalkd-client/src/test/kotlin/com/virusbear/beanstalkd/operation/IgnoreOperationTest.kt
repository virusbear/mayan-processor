package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.NotIgnoredException
import com.virusbear.beanstalkd.response.NotIgnoredResponse
import com.virusbear.beanstalkd.response.WatchingResponse
import com.virusbear.beanstalkd.writePacketAsText
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.Test

class IgnoreOperationTest {
    @Test
    fun write() = runBlocking {
        val tube = "default"

        val op = IgnoreOperation(tube)

        val packet = op.writePacketAsText()

        assertEquals("ignore default\r\n", packet)
    }

    @Test
    fun readWatchingResponse(): Unit = runBlocking {
        val tube = "default"

        val op = IgnoreOperation(tube)

        val watchingCount = 5u
        op.read(WatchingResponse(watchingCount))
        val count = op.await()

        assertEquals(watchingCount, count)
    }

    @Test
    fun readNotIgnoredResponse(): Unit = runBlocking {
        val tube = "default"

        val op = IgnoreOperation(tube)

        val exception = op.readResponse(NotIgnoredResponse).exceptionOrNull()
        assertNotNull(exception)
        assertIs<NotIgnoredException>(exception)
        assertEquals(tube, exception.tube)
    }
}