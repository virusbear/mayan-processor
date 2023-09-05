package com.virusbear.beanstalkd

import com.virusbear.beanstalkd.operation.Operation
import com.virusbear.beanstalkd.response.ResponseType
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DefaultConnection(
    host: String,
    port: Int,
    private val responseTypes: Set<ResponseType>
): Connection {
    private val mutex = Mutex()
    private val selectorManager = SelectorManager(Dispatchers.IO)
    private var socket: Socket

    private var readChannel: ByteReadChannel
    private var writeChannel: ByteWriteChannel

    init {
        socket = runBlocking { aSocket(selectorManager).tcp().connect(host, port) }

        readChannel = socket.openReadChannel()
        writeChannel = socket.openWriteChannel()
    }

    override suspend fun <T> handle(operation: Operation<T>): Result<T> =
        //TODO: add current task to internal operation queue.
        //TODO: use background Job to process this queue
        //TODO: notify about completeness of operation using some kind of event or callback.
        //TODO: return as soon as queue has completed executing the operation
        //TODO: are there any operations that need to be executed in order?
        //TODO: do we want to have some kind of "PipelineOperation" being able to combine multiple operations executed one after the other in order of definition?
        mutex.withLock {
            if(socket.isClosed || readChannel.isClosedForRead || writeChannel.isClosedForWrite) {
                return@withLock Result.failure(DisconnectedException())
            }

            writeChannel.writePacketSuspend {
                operation.write(this)
            }
            writeChannel.flush()

            val responseParams = readChannel.readUTF8Line()?.split(" ") ?: emptyList()
            if(responseParams.isEmpty()) {
                return@withLock Result.failure(InvalidResponseException())
            }

            val code = responseParams.first()
            val params = responseParams.drop(1)
            val responseType = responseTypes.firstOrNull { it.code == code } ?: return@withLock Result.failure(UnknownResponseException(code))

            val response = responseType.read(params, readChannel)
            operation.read(response)
        }

    override fun close() {
        socket.close()
    }
}