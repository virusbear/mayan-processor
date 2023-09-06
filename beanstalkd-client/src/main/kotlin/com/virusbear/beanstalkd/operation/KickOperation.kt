package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.NotFoundException
import com.virusbear.beanstalkd.response.KickedResponse
import com.virusbear.beanstalkd.response.NotFoundResponse
import com.virusbear.beanstalkd.response.Response
import io.ktor.utils.io.core.*

class KickOperation(
    val bound: UInt? = null,
    val id: UInt? = null
): AbstractOperation<UInt>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("kick")

        bound?.let {
            packet.append(" $bound")
        } ?: id?.let {
            packet.append("-job $id")
        } ?: error("neither 'bound' nor 'id' specified for kick operation")

        packet.append("\r\n")
    }

    override suspend fun readResponse(response: Response): Result<UInt> =
        when(response) {
            is KickedResponse -> Result.success(response.count)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}