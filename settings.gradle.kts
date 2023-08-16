pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        gradlePluginPortal()
    }
}
rootProject.name = "mayan-processor"
include("script-definition")
include("script-host")
include("script-api")
include("processor-service")
include("mayan-client")
include("mayan-script-impl")
