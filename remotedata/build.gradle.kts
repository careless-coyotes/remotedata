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
                implementation(kotlin("test-testng"))
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
