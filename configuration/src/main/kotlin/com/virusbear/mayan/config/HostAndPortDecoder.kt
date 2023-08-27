package com.virusbear.mayan.config

import com.sksamuel.hoplite.*
import com.sksamuel.hoplite.decoder.Decoder
import com.sksamuel.hoplite.fp.flatMap
import com.sksamuel.hoplite.fp.invalid
import com.sksamuel.hoplite.fp.valid
import kotlin.reflect.KType
import kotlin.reflect.full.createType

internal object HostAndPortDecoder: Decoder<HostAndPort> {
    override fun decode(node: Node, type: KType, context: DecoderContext): ConfigResult<HostAndPort> =
        when(node) {
            is StringNode -> {
                var host = node.value.substringBefore(":")
                val port = node.value.substringAfter(":")

                if(host.isEmpty()) {
                    host = "0.0.0.0"
                }

                when(val portValue = port.toIntOrNull()) {
                    null -> ConfigFailure.DecodeError(node, type).invalid()
                    else -> HostAndPort(host, portValue).valid()
                }
            }
            else -> ConfigFailure.DecodeError(node, type).invalid()
        }

    override fun supports(type: KType): Boolean =
        type == HostAndPort::class.createType()
}