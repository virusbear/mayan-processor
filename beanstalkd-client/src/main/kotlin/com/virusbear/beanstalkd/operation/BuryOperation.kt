package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.NotFoundException
import com.virusbear.beanstalkd.response.BuriedResponse
import com.virusbear.beanstalkd.response.NotFoundResponse
import com.virusbear.beanstalkd.response.Response
import io.ktor.utils.io.core.*

class BuryOperation(
    val id: UInt,
    val priority: UInt
): AbstractOperation<Unit>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("bury $id $priority\r\n")
    }

    override suspend fun read(response: Response): Result<Unit> =
        when(response) {
            is BuriedResponse -> Result.success(Unit)
            is NotFoundResponse -> Result.failure(NotFoundException())
            else -> super.read(response)
        }
}