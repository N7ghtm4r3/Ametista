plugins {
    id("java")
    id("org.springframework.boot") version "3.2.3"
    id("maven-publish")
    kotlin("jvm")
}

apply(plugin = "io.spring.dependency-management")

group = "com.tecknobit"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.clojars.org")
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.3")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.3")
    implementation("org.springframework.boot:spring-boot-maven-plugin:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.3")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.github.N7ghtm4r3:APIManager:2.2.4")
    implementation("io.github.n7ghtm4r3:Equinox:1.0.4")
    implementation("com.github.N7ghtm4r3:Mantis:1.0.0")
    implementation("org.json:json:20240303")
    implementation("commons-validator:commons-validator:1.7")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.tecknobit.ametistacore"
                artifactId = "ametistacore"
                version = "1.0.0"
                from(components["java"])
            }
        }
    }
}

configurations.all {
    exclude("commons-logging", "commons-logging")
}

kotlin {
    jvmToolchain(18)
}
