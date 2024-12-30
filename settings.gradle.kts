pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io") // Добавляем JitPack
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Или ALLOW_REPOS
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io") // Добавляем JitPack
    }
}

rootProject.name = "AmarantBonusCompose"
include(":app")
