package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.Job
import com.virusbear.beanstalkd.JobState
import com.virusbear.beanstalkd.response.Response
import io.ktor.utils.io.core.*

class PeekOperation(
    val id: UInt? = null,
    val state: JobState? = null
): AbstractOperation<Job>() {
    override suspend fun write(packet: BytePacketBuilder) {
        packet.append("peek")

        id?.let {
            packet.append(" $id")
        } ?: state?.let {
            when(state) {
                JobState.Ready -> packet.append("-ready")
                JobState.Delayed -> packet.append("-delayed")
                JobState.Buried -> packet.append("-buried")
                else -> error("State $state not supported in peek operations")
            }
        } ?: error("neither 'state' nor 'id' specified for peek operation")

        packet.append("\r\n")
    }

    override suspend fun read(response: Response): Result<Job> {
        return super.read(response)
    }
}