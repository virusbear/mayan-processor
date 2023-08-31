package com.virusbear.beanstalkd

import kotlin.time.Duration

//TODO: undecided if "*Result" return values are what is wanted here. they provide a nice possibility to avoid errors but also make api more complex
//Should "success" values be returned instead and exceptions be thrown otherwise?
//If exceptions, how to deal with "Deadline Soon" status returned by reserve?
interface Client {
    suspend fun put(priority: UInt = 0u, delay: UInt = 0u, ttr: UInt = UInt.MAX_VALUE, data: ByteArray): PutResult
    suspend fun use(tube: String)
    suspend fun reserve(): ReserveResult
    suspend fun reserveWithTimeout(timeout: Duration): ReserveResult
    suspend fun reserveJob(id: UInt): ReserveResult
    suspend fun delete(id: UInt)//: DeleteResult -> DELETED, NOT_FOUND
    suspend fun release(id: UInt, priority: UInt, delay: UInt)//: ReleaseResult -> RELEASED, BURIED, NOT_FOUND
    suspend fun bury(id: UInt, priority: UInt)//: BuryResult -> BURIED, NOT_FOUND
    suspend fun touch(id: UInt)//: TouchResult -> TOUCHED, NOT_FOUND
    suspend fun watch(tube: String)//: WatchResult -> WATCHING
    suspend fun ignore(tube: String)//: IgnoreResult -> WATCHING, NOT_IGNORED
    suspend fun peek(id: UInt)
    suspend fun peekReady()
    suspend fun peekDelayed()
    suspend fun peekBuried()
    suspend fun kick(bound: UInt)
    suspend fun kickJob(id: UInt)
    suspend fun statsJob(id: UInt)
    suspend fun statsTube(tube: String)
    suspend fun stats()
    suspend fun listTubes()
    suspend fun listTubeUsed()
    suspend fun listTubsWatched()
    suspend fun quit()
    suspend fun pauseTube(tubeName: String, delay: UInt)
}