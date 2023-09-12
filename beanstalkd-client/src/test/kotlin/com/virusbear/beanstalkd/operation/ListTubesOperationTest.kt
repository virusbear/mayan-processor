package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.response.OkResponse
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ListTubesOperationTest {
    @Test
    fun `write list all tubes`(): Unit = runBlocking {
        val op = ListTubesOperation()

        val packet = BytePacketBuilder().apply { op.write(this) }.build().readText()

        assertEquals("list-tubes\r\n", packet)
    }

    @Test
    fun `write list watched tubes`(): Unit = runBlocking {
        val op = ListTubesOperation(watched = true)

        val packet = BytePacketBuilder().apply { op.write(this) }.build().readText()

        assertEquals("list-tubes-watched\r\n", packet)
    }

    @Test
    fun read(): Unit = runBlocking {
        val op = ListTubesOperation()

        val responseTubes = mapOf(
            "default" to "",
            "test" to ""
        )

        op.read(OkResponse(responseTubes))
        val tubes = op.await()

        assertContentEquals(responseTubes.keys.toList(), tubes)
    }
}
