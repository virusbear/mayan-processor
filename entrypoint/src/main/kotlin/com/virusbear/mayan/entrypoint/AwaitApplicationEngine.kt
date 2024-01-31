package com.virusbear.mayan.entrypoint

import io.ktor.server.application.*
import io.ktor.server.engine.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun ApplicationEngine.await() =
    suspendCancellableCoroutine { cont ->
        environment.monitor.subscribe(ApplicationStopped) {
            cont.resume(Unit)
        }

        cont.invokeOnCancellation {
            stop()
        }
    }