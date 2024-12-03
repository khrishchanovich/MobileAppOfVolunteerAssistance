pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven ( "https://storage.zego.im/maven" )   // <- Add this line.
        maven ( "https://jitpack.io" )  // <- Add this line.
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://storage.zego.im/maven")  // Репозиторий Zego
        maven("https://jitpack.io")
    }
}

rootProject.name = "VolunteerAssistance"
include(":app")
