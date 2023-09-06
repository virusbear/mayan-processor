package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.NotFoundException
import com.virusbear.beanstalkd.response.NotFoundResponse
import com.virusbear.beanstalkd.response.Response
import com.virusbear.beanstalkd.response.TouchedResponse
import io.ktor.utils.io.core.*

class TouchOperation(
    val id: UInt
): AbstractOperation<Unit>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("touche $id\r\n")
    }

    override suspend fun readResponse(response: Response): Result<Unit> =
        when(response) {
            is TouchedResponse -> Result.success(Unit)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.readResponse(response)
        }
}