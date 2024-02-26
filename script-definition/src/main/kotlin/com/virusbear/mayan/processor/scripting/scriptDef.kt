package com.virusbear.mayan.processor.scripting

import com.virusbear.mayan.processor.MayanProcessorBuilder
import java.io.File
import java.io.FileNotFoundException
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.FileScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.util.PropertiesCollection
import kotlin.script.experimental.util.getOrError

@KotlinScript(
    fileExtension = "mayan.kts",
    compilationConfiguration = MayanProcessorScriptConfiguration::class
)
abstract class MayanProcessorScript

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FILE)
@Repeatable
annotation class Use(val library: String)

val LibraryDirectoryProperty by PropertiesCollection.key<File>()

object MayanProcessorScriptConfiguration: ScriptCompilationConfiguration(
    {
        defaultImports(Use::class)
        jvm {
            dependenciesFromCurrentContext("script-definition", "script-api")
        }
        refineConfiguration {
            onAnnotations<Use> { context ->
                val annotations = context.collectedData?.get(ScriptCollectedData.collectedAnnotations)?.takeIf { it.isNotEmpty() } ?: return@onAnnotations context.compilationConfiguration.asSuccess()
                val libraryDirectory = context.compilationConfiguration.getOrError(LibraryDirectoryProperty)

                val libraries = annotations.map {
                    val useAnnotation = it.annotation as Use

                    val libraryPath = libraryDirectory.resolve("${File(useAnnotation.library).name}.lib.mayan.kts")
                    if(!libraryPath.exists()) {
                        return@onAnnotations makeFailureResult(
                            ScriptDiagnostic(
                                0,
                                "Library ${useAnnotation.library} not found",
                                locationWithId = it.location,
                                exception = FileNotFoundException(libraryPath.relativeTo(libraryDirectory).path)
                            )
                        )
                    }

                    FileScriptSource(libraryPath)
                }

                context.compilationConfiguration.with {
                    importScripts.append(libraries)
                }.asSuccess()
            }
        }
        implicitReceivers(MayanProcessorBuilder::class)
    }
)