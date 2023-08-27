package com.virusbear.mayan.config

import com.sksamuel.hoplite.*
import java.io.File
import kotlin.reflect.KClass

class ConfigLoader(
    args: Array<String>,
    configFile: File = File("./config.yaml")
) {
    private val loader =
        ConfigLoaderBuilder
            .default()
            .addPreprocessor(DockerSecretPreprocessor)
            .addDecoder(HostAndPortDecoder)
            .addFileSource(configFile, optional = true, allowEmpty = true)
            .addEnvironmentSource()
            .addCommandLineSource(args)
            .build()

    inline fun <reified T: Any> load(): Result<T> =
        load(T::class)

    fun <T: Any> load(klass: KClass<T>): Result<T> =
        try {
            Result.success(loader.loadConfigOrThrow(klass, emptyList()))
        } catch(ex: ConfigException) {
            Result.failure(ex)
        }
}