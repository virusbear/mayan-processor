package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.NotIgnoredException
import com.virusbear.beanstalkd.response.NotIgnoredResponse
import com.virusbear.beanstalkd.response.Response
import com.virusbear.beanstalkd.response.WatchingResponse
import io.ktor.utils.io.core.*

class IgnoreOperation(
    val tube: String
): AbstractOperation<UInt>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("ignore $tube\r\n")
    }

    override suspend fun read(response: Response): Result<UInt> =
        when(response) {
            is WatchingResponse -> Result.success(response.count)
            is NotIgnoredResponse -> Result.failure(NotIgnoredException(tube))
            else -> super.read(response)
        }
}