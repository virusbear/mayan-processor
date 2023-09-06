package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.response.Response
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Deferred

interface Operation<out T>: Deferred<T> {
    suspend fun write(packet: BytePacketBuilder)
    suspend fun read(response: Response)
}