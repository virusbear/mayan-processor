package com.virusbear.beanstalkd

import com.virusbear.beanstalkd.operation.PutOperation
import com.virusbear.beanstalkd.operation.ReserveOperation
import com.virusbear.beanstalkd.operation.UseOperation
import com.virusbear.beanstalkd.response.*
import kotlin.time.Duration

class DefaultClient(
    host: String = "localhost",
    port: Int = 11300
): Client {
    val connection: Connection =
        DefaultConnection(
            host,
            port,
            setOf(
                OutOfMemoryResponse,
                InternalErrorResponse,
                BadFormatResponse,
                UnknownCommandResponse,
                ExpectedCrlfResponse,
                JobTooBigResponse,
                DrainingResponse,
                InsertedResponse,
                BuriedResponse,
                UsingResponse,
                ReservedResponse,
                DeadlineSoonResponse,
                TimedOutResponse,
                NotFoundResponse
            )
        )

    override suspend fun put(data: ByteArray, priority: UInt, delay: UInt, ttr: UInt): UInt =
        connection.handle(PutOperation(priority, delay, ttr, data)).getOrThrow()

    override suspend fun use(tube: String) {
        connection.handle(UseOperation(tube)).getOrThrow()
    }

    override suspend fun reserve(): Job =
        connection.handle(ReserveOperation()).getOrThrow()

    override suspend fun reserveWithTimeout(timeout: Duration): Job =
        connection.handle(ReserveOperation(timeout = timeout)).getOrThrow()

    override suspend fun reserveJob(id: UInt): Job =
        connection.handle(ReserveOperation(jobId = id)).getOrThrow()

    override suspend fun delete(id: UInt): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun release(id: UInt, priority: UInt, delay: UInt): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun bury(id: UInt, priority: UInt): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun touch(id: UInt): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun watch(tube: String) {
        TODO("Not yet implemented")
    }

    override suspend fun ignore(tube: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun peek(id: UInt): Job? {
        TODO("Not yet implemented")
    }

    override suspend fun peekReady(): Job? {
        TODO("Not yet implemented")
    }

    override suspend fun peekDelayed(): Job? {
        TODO("Not yet implemented")
    }

    override suspend fun peekBuried(): Job? {
        TODO("Not yet implemented")
    }

    override suspend fun kick(bound: UInt): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun kickJob(id: UInt): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun statsJob(id: UInt): Map<String, String> {
        TODO("Not yet implemented")
    }

    override suspend fun statsTube(tube: String): Map<String, String> {
        TODO("Not yet implemented")
    }

    override suspend fun stats(): Map<String, String> {
        TODO("Not yet implemented")
    }

    override suspend fun listTubes(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun listTubeUsed(): String {
        TODO("Not yet implemented")
    }

    override suspend fun listTubesWatched(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun quit() {
        TODO("Not yet implemented")
    }

    override suspend fun pauseTube(tubeName: String, delay: UInt): Boolean {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}