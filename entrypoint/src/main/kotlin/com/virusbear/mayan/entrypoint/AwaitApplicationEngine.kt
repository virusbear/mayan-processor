package com.virusbear.mayan.entrypoint

import io.ktor.server.engine.*
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun ApplicationEngine.await() =
    suspendCancellableCoroutine<Unit> {
        addShutdownHook {
            it.cancel()
        }

        it.invokeOnCancellation {
            stop()
        }
    }