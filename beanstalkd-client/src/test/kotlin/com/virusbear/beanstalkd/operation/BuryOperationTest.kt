package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.NotFoundException
import com.virusbear.beanstalkd.response.BuriedResponse
import com.virusbear.beanstalkd.response.NotFoundResponse
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class BuryOperationTest {
    @Test
    fun write() = runBlocking {
        val id = 712946673u
        val priority = 2870041978u

        val op = BuryOperation(id, priority)

        val packet = BytePacketBuilder().apply { op.write(this) }.build().readText()

        assertEquals("bury $id $priority\r\n", packet)
    }

    @Test
    fun read(): Unit = runBlocking {
        val id = 712946673u
        val priority = 2870041978u

        val op = BuryOperation(id, priority)

        op.read(BuriedResponse(id))
        op.await()

        val exception = op.readResponse(NotFoundResponse).exceptionOrNull()
        assertNotNull(exception)
        assertIs<NotFoundException>(exception)
    }
}