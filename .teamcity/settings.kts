import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.StageFactory.parallel
import jetbrains.buildServer.configs.kotlin.StageFactory.sequential
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.qodana
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.05"

//TODO:
//2: Nightly build of develop (only if changes were made) -> release as snapshot version
//3: after merge on master -> release as release version -> update docker image with :latest tag
//4: after each commit on "release" branch -> release "RCx" version -> create docker image
//5: run qodana+detekt on every push to any branch
//6: build any branch (only kotlin) after each push

//IMPORTANT: only do releases in case build was successful

val jdk = "%env.JDK_17_0_x64%"

project {
    buildType(Build)
    buildType(Qodana)
    buildType(IdeaInspections)
    buildType(IdeaDuplicates)
}

object IdeaDuplicates: BuildType({
    name = "Idea Duplicates"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        ideaDuplicates {
            pathToProject = "build.gradle.kts"
            jvmArgs = "-Xmx1G -XX:ReservedCodeCacheSize=512m"
            targetJdkHome = jdk
            ideaAppHome = "%teamcity.tool.intellij.ideaIU-2023.1.2%"
            lowerBound = 10
            discardCost = 0
            distinguishMethods = true
            distinguishTypes = true
            distinguishLiterals = true
            extractSubexpressions = true
        }
    }

    features {
        perfmon {
        }
    }
})

object IdeaInspections: BuildType({
    name = "Idea Inspections"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        ideaInspections {
            pathToProject = "build.gradle.kts"
            jvmArgs = "-Xmx1024m -XX:ReservedCodeCacheSize=512m"
            targetJdkHome = jdk
            ideaAppHome = "%teamcity.tool.intellij.ideaIU-2023.1.2%"
        }
    }

    features {
        perfmon {
        }
    }
})

object Qodana: BuildType({
    name = "Qodana"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        qodana {
            linter = jvm {
            }
        }
    }

    features {
        perfmon {
        }
    }
})

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            tasks = "clean build"
            jdkHome = jdk
        }
    }

    features {
        perfmon {
        }
    }
})
