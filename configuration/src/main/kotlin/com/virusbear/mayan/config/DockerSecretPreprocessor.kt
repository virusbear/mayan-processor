package com.virusbear.mayan.config

import com.sksamuel.hoplite.*
import com.sksamuel.hoplite.fp.invalid
import com.sksamuel.hoplite.fp.valid
import com.sksamuel.hoplite.preprocessor.Preprocessor
import java.io.File
import java.util.*

internal object DockerSecretPreprocessor: Preprocessor {
    private val schemeRegex = Regex("secret://(.+)")
    private val templateRegex = Regex("\\{\\{ ?secret.(.+?) ?\\}\\}")

    private fun readDockerSecret(secret: String): Result<Optional<String>> = runCatching {
        val secretFile = File("/run/secrets/$secret")

        if(!secretFile.canRead()) {
            Optional.empty()
        } else {
            Optional.ofNullable(secretFile.readLines().firstOrNull())
        }
    }

    override fun process(node: Node, context: DecoderContext): ConfigResult<Node> =
        when(node) {
            is StringNode -> when(val match = schemeRegex.matchEntire(node.value) ?: templateRegex.matchEntire(node.value)) {
                null -> node.valid()
                else -> {
                    val secret = match.groupValues[1]

                    readDockerSecret(secret).fold(
                        {
                            when(val v = it.orElseGet { null }) {
                                null -> ConfigFailure.PreprocessorWarning("Secret '$secret' not found").invalid()
                                else -> node.copy(value = v).withMeta(CommonMetadata.UnprocessedValue, node.value).valid()
                            }
                        },
                        {
                            ConfigFailure.PreprocessorFailure("Failed loading docker secret '$secret'", it).invalid()
                        }
                    )
                }
            }
            else -> node.valid()
        }
}