plugins {
    kotlin("jvm")
}

val coroutines_version: String by project
val logback_version: String by project
val logging_version: String by project

dependencies {
    implementation("io.github.microutils:kotlin-logging:$logging_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    implementation(project(":configuration"))
    implementation(project(":entrypoint"))
    implementation(project(":worker"))
    implementation(project(":mayan-client"))
    implementation("org.redisson:redisson:3.23.1")
    implementation("com.squareup.tape2:tape:2.0.0-beta1")
}