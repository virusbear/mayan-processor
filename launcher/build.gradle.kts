plugins {
    kotlin("jvm")
    application
}

val coroutines_version: String by project
val logback_version: String by project
val logging_version: String by project

dependencies {
    implementation("io.github.microutils:kotlin-logging:$logging_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("com.virusbear:beanstalkt:1.1.0")

    implementation(project(":configuration"))
    implementation(project(":entrypoint"))
    implementation(project(":worker"))
    implementation(project(":mayan-client"))
}

application {
    mainClass.set("com.virusbear.mayan.launcher.LauncherKt")
}