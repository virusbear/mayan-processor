package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.*
import com.virusbear.beanstalkd.response.*
import com.virusbear.beanstalkd.writePacketAsText
import kotlinx.coroutines.runBlocking
import kotlin.test.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.parseIsoStringOrNull
import kotlin.time.Duration.Companion.seconds

class PausedOperationTest {
    @Test
    fun write() = runBlocking {
        val tube = "default"
        val delay = 10.seconds
        val op = PausedOperation(tube, delay)

        val packet = op.writePacketAsText()

        assertEquals("pause-tube $tube ${delay.inWholeSeconds}\r\n", packet)
    }

    @Test
    fun `read paused response`(): Unit = runBlocking {
        val op = PausedOperation("", Duration.ZERO)

        val exception = op.readResponse(PausedResponse).exceptionOrNull()

        assertNull(exception)
    }

    @Test
    fun `read not found response`(): Unit = runBlocking {
        val op = PausedOperation("", Duration.ZERO)

        val exception = op.readResponse(NotFoundResponse).exceptionOrNull()

        assertNotNull(exception)
        assertIs<NotFoundException>(exception)
    }
}
class PutOperationTest {
    @Test
    fun write() = runBlocking {
        val data = "Hello World!"
        val priority = 697756u
        val delay = 939.24.seconds
        val ttr = 290.25.seconds

        val op = PutOperation(priority, delay, ttr, data.toByteArray())

        val packet = op.writePacketAsText()

        assertEquals("put $priority ${delay.inWholeSeconds} ${ttr.inWholeSeconds} ${data.toByteArray().size}\r\n$data\r\n", packet)
    }

    @Test
    fun `read inserted response`(): Unit = runBlocking {
        val op = PutOperation(0u, Duration.ZERO, Duration.ZERO, byteArrayOf())
        val id = 295294u

        op.read(InsertedResponse(id))
        val result = op.await()

        assertEquals(id, result)
    }

    @Test
    fun `read buried response with id`(): Unit = runBlocking {
        val op = PutOperation(0u, Duration.ZERO, Duration.ZERO, byteArrayOf())
        val id = 462997u

        val exception = op.readResponse(BuriedResponse(id)).exceptionOrNull()

        assertNotNull(exception)
        assertIs<BuriedException>(exception)
        assertEquals(UInt.MAX_VALUE, exception.id)
    }

    @Test
    fun `read buried response without id`(): Unit = runBlocking {
        val op = PutOperation(0u, Duration.ZERO, Duration.ZERO, byteArrayOf())

        val exception = op.readResponse(BuriedResponse()).exceptionOrNull()

        assertNotNull(exception)
        assertIs<BuriedException>(exception)
        assertEquals(UInt.MAX_VALUE, exception.id)
    }

    @Test
    fun `read expected crlf response`(): Unit = runBlocking {
        val op = PutOperation(0u, Duration.ZERO, Duration.ZERO, byteArrayOf())

        val exception = op.readResponse(BuriedResponse()).exceptionOrNull()

        assertNotNull(exception)
        assertIs<ExpectedCrlfException>(exception)
    }

    @Test
    fun `read job too big response`(): Unit = runBlocking {
        val op = PutOperation(0u, Duration.ZERO, Duration.ZERO, byteArrayOf())

        val exception = op.readResponse(JobTooBigResponse).exceptionOrNull()

        assertNotNull(exception)
        assertIs<JobTooBigException>(exception)
    }

    @Test
    fun `read draining response`(): Unit = runBlocking {
        val op = PutOperation(0u, Duration.ZERO, Duration.ZERO, byteArrayOf())

        val exception = op.readResponse(DrainingResponse).exceptionOrNull()

        assertNotNull(exception)
        assertIs<DrainingException>(exception)
    }
}