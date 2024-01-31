import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.openapi.generator") version "7.2.0"
}

val coroutines_version: String by project

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    api("com.squareup.okhttp3:okhttp:4.10.0")
    api("com.squareup.moshi:moshi:1.15.0")
    api("com.squareup.moshi:moshi-kotlin:1.15.0")
}

val generatedSourcesPath = "$buildDir/generated"
val apiDescriptionFile = "$projectDir/src/main/resources/mayan-edms-v4-swagger2.0.json"
val apiRootName = "com.virusbear.mayan.api.client"

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set(apiDescriptionFile)
    outputDir.set("$buildDir/generated")
    apiPackage.set("$apiRootName.api")
    modelPackage.set("$apiRootName.model")
    packageName.set(apiRootName)

    configOptions = mapOf(
        "enumPropertyNaming" to "PascalCase",
        "sortParamsByRequiredFlag" to "true",
        "useCoroutines" to "true",
    )
}

sourceSets {
    main {
        kotlin {
            srcDir("$generatedSourcesPath/src/main/kotlin")
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    dependsOn("openApiGenerate")
}