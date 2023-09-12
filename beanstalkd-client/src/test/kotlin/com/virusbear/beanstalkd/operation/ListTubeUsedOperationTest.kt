package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.response.UsingResponse
import com.virusbear.beanstalkd.writePacketAsText
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ListTubeUsedOperationTest {
    @Test
    fun write() = runBlocking {
        val op = ListTubeUsedOperation()

        val packet = op.writePacketAsText()

        assertEquals("list-tube-used\r\n", packet)
    }

    @Test
    fun read() = runBlocking {
        val tube = "default"
        val op = ListTubeUsedOperation()
        val response = UsingResponse(tube)

        op.read(response)
        val using = op.await()

        assertEquals(tube, using)
    }
}
