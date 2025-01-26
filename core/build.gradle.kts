plugins {
    id("java")
    id("maven-publish")
    kotlin("jvm")
}

group = "com.tecknobit"
version = "1.0.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.clojars.org")
}

dependencies {
    implementation("com.github.N7ghtm4r3:APIManager:2.2.4")
    implementation("io.github.n7ghtm4r3:equinox-core:1.0.6")
    implementation("com.github.N7ghtm4r3:Mantis:1.0.0")
    implementation("org.json:json:20240303")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.tecknobit.ametistacore"
                artifactId = "ametistacore"
                version = "1.0.1"
                from(components["java"])
            }
        }
    }
}

kotlin {
    jvmToolchain(18)
}
