package com.virusbear.mayan.entrypoint

import com.virusbear.mayan.processor.worker.MayanTask
import com.virusbear.mayan.processor.worker.MayanTaskQueue
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend fun EntryPoint(
    queue: MayanTaskQueue,
    config: EntryPointConfig,
    context: CoroutineContext = EmptyCoroutineContext
) {
    withContext(context + CoroutineName("EntryPoint")) {
        embeddedServer(Netty, host = config.bind.host, port = config.bind.port){
            routing {
                get("enqueue/{documentId}") {
                    val documentId = call.parameters["documentId"]?.toIntOrNull()

                    documentId?.let {
                        queue.send(MayanTask(documentId))
                        call.respond(HttpStatusCode.Accepted)
                    } ?: call.respond(HttpStatusCode.BadRequest)
                }
            }
        }.start().await()
    }
}