pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
rootProject.name = "mayan-processor"
include("script-definition")
include("script-host")
include("script-api")
include("mayan-client")
include("mayan-script-impl")
include("worker")
include("entrypoint")
include("launcher")
include("configuration")
include("mayan-api")
