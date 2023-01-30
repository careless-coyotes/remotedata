plugins {
    kotlin("multiplatform")
    `maven-publish`
    signing
}

group = "com.carelesscoyotes.remotedata"
version = "0.2"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    linuxX64()
    macosArm64()

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("com.willowtreeapps.assertk:assertk:0.25")
            }
        }
        named("jvmTest") {
            dependencies {
                implementation(kotlin("test-testng"))
            }
        }
    }
}


tasks.withType<Test> {
    useTestNG()
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
