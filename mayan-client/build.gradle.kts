plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.0"
}

val kotlin_version = "1.9.0"
val coroutines_version = "1.6.4"
val serialization_version = "1.3.3"
val ktor_version = "2.1.3"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serialization_version")

    api("io.ktor:ktor-client-core:$ktor_version")
    api("io.ktor:ktor-client-serialization:$ktor_version")
    api("io.ktor:ktor-client-content-negotiation:$ktor_version")
    api("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    implementation("io.ktor:ktor-client-cio-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-jetty-jvm:$ktor_version")
}