package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.response.Response
import io.ktor.utils.io.core.*

interface Operation<T> {
    suspend fun write(packet: BytePacketBuilder)
    suspend fun read(response: Response): Result<T>
}