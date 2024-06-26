plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
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
    macosArm64()
    macosX64()
    iosArm64()
    iosX64()
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
                implementation(kotlin("test"))
                implementation(compose.desktop.uiTestJUnit4)
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
