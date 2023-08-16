import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0" apply false
}

subprojects {
    group = "com.virusbear"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
