package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.response.UsingResponse
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class ListTubeUsedOperationTest {
    @Test
    fun write() = runBlocking {
        val op = ListTubeUsedOperation()

        val packet = BytePacketBuilder().apply { op.write(this) }.build().readText()

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
