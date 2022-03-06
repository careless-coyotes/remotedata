import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    alias(libs.plugins.compose)
}

version = "0.1"

repositories {
    google()
    mavenCentral()
}

kotlin {
    android()
    jvm("desktop")

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

        named("androidMain") {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        named("desktopTest") {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(@OptIn(ExperimentalComposeLibrary::class) compose.uiTestJUnit4)
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    compileSdk = 32

    defaultConfig {
        @Suppress("UnstableApiUsage")
        minSdk = 21
        @Suppress("UnstableApiUsage")
        targetSdk = 32
    }

    @Suppress("UnstableApiUsage")
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}
