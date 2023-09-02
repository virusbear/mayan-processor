package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.*
import com.virusbear.beanstalkd.response.*
import io.ktor.utils.io.core.*

class PutOperation(
    val priority: UInt,
    val delay: UInt,
    val ttr: UInt,
    val data: ByteArray
): AbstractOperation<UInt>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("put $priority $delay $ttr ${data.size}\r\n")
        packet.writeFully(data)
        packet.append("\r\n")
    }

    override suspend fun read(response: Response): Result<UInt> =
        when(response) {
            is InsertedResponse -> Result.success(response.id)
            is BuriedResponse -> Result.failure(BuriedException(response.id))
            is ExpectedCrlfResponse -> Result.failure(ExpectedCrlfException())
            is JobTooBigResponse -> Result.failure(JobTooBigException())
            is DrainingResponse -> Result.failure(DrainingException())
            else -> super.read(response)
        }
}

