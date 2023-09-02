package com.virusbear.beanstalkd

import java.io.Closeable
import kotlin.time.Duration

interface Client: Closeable {
    suspend fun put(data: ByteArray, priority: UInt = 0u, delay: UInt = 0u, ttr: UInt = UInt.MAX_VALUE): UInt
    suspend fun use(tube: String)
    suspend fun reserve(): Job
    suspend fun reserveWithTimeout(timeout: Duration): Job
    suspend fun reserveJob(id: UInt): Job
    suspend fun delete(id: UInt): Boolean
    suspend fun release(id: UInt, priority: UInt, delay: UInt): Boolean
    suspend fun bury(id: UInt, priority: UInt): Boolean
    suspend fun touch(id: UInt): Boolean
    suspend fun watch(tube: String)
    suspend fun ignore(tube: String): Boolean
    suspend fun peek(id: UInt): Job?
    suspend fun peekReady(): Job?
    suspend fun peekDelayed(): Job?
    suspend fun peekBuried(): Job?
    suspend fun kick(bound: UInt): Boolean
    suspend fun kickJob(id: UInt): Boolean
    suspend fun statsJob(id: UInt): Map<String, String>
    suspend fun statsTube(tube: String): Map<String, String>
    suspend fun stats(): Map<String, String>
    suspend fun listTubes(): List<String>
    suspend fun listTubeUsed(): String
    suspend fun listTubesWatched(): List<String>
    suspend fun quit()
    suspend fun pauseTube(tubeName: String, delay: UInt): Boolean
}