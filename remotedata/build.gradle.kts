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
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.assertk)
            }
        }
        named("jvmTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }

    jvmToolchain(11)
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
