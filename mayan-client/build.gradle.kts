plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val coroutines_version: String by project
val logging_version: String by project

dependencies {
    implementation("io.github.microutils:kotlin-logging:$logging_version")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7")
    implementation("io.ktor:ktor-client-auth:2.3.7")
    implementation("io.ktor:ktor-client-encoding:2.3.7")
    implementation("io.ktor:ktor-client-resources:2.3.7")
    implementation("io.ktor:ktor-client-logging:2.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
}