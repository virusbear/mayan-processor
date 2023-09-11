package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.NotFoundException
import com.virusbear.beanstalkd.response.DeletedResponse
import com.virusbear.beanstalkd.response.NotFoundResponse
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class DeleteOperationTest {
    @Test
    fun write() = runBlocking {
        val id = 3397947682u

        val op = DeleteOperation(id)

        val packet = BytePacketBuilder().apply { op.write(this) }.build().readText()

        assertEquals("delete $id\r\n", packet)
    }

    @Test
    fun read(): Unit = runBlocking {
        val id = 2415865491u

        val op = DeleteOperation(id)

        op.read(DeletedResponse)
        op.await()

        val exception = op.readResponse(NotFoundResponse).exceptionOrNull()
        assertNotNull(exception)
        assertIs<NotFoundException>(exception)
    }
}