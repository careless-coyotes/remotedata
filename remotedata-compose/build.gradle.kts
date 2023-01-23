import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    `maven-publish`
}

version = "0.1"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm()

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(compose.runtime)
                api(project(":remotedata"))
            }
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.mockk)
            }
        }

        named("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(@OptIn(ExperimentalComposeLibrary::class) compose.uiTestJUnit4)
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
