plugins {
    kotlin("multiplatform")
    `maven-publish`
    signing
}

group = "com.carelesscoyotes.remotedata"
version = "0.3"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    linuxArm64()
    linuxX64()
    macosArm64()
    macosX64()
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.kotlinx.coroutines.core)
                api(project(":remotedata"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.assertk)
            }
        }
    }

    jvmToolchain(11)
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
