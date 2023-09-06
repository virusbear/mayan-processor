package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.response.Response
import com.virusbear.beanstalkd.response.WatchingResponse
import io.ktor.utils.io.core.*

class WatchOperation(
    val tube: String
): AbstractOperation<UInt>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("watch $tube\r\n")
    }

    override suspend fun readResponse(response: Response): Result<UInt> =
        when(response) {
            is WatchingResponse -> Result.success(response.count)
            else -> super.readResponse(response)
        }
}