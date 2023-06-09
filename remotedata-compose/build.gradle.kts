import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    `maven-publish`
    signing
}

group = "com.carelesscoyotes.remotedata"
version = "0.2"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm()
    macosX64()
    macosArm64()
    ios()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(compose.runtime)
                implementation(compose.foundation)
                api(project(":remotedata"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
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

    jvmToolchain(11)
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
