plugins {
    id("java")
    id("org.springframework.boot") version "3.2.3"
    kotlin("jvm")
}

apply(plugin = "io.spring.dependency-management")

group = "com.tecknobit"
version = "1.0.2"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.clojars.org")
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.maven.plugin)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.mysql.connector.java)
    implementation(libs.apimanager)
    implementation(libs.equinox.backend)
    implementation(libs.equinox.core)
    implementation(libs.json)
    implementation(project(":core"))
}