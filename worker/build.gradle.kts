plugins {
    kotlin("jvm")
}

val coroutines_version: String by project
val logging_version: String by project

dependencies {
    implementation("io.github.microutils:kotlin-logging:$logging_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    implementation(project(":mayan-script-impl"))
    implementation("org.redisson:redisson:3.23.1")
}