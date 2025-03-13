pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.0"
        kotlin("multiplatform") version "2.1.0"
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "Ametista"
include("core")
include("backend")
