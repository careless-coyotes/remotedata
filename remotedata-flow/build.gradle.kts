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
    linuxX64()
    linuxArm64()
    macosX64()
    macosArm64()
    ios()
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
                implementation(kotlin("test-testng"))
                implementation(libs.assertk)
            }
        }
    }

    jvmToolchain(11)
}

tasks.withType<Test> {
    useTestNG()
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
