plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
    signing
}

group = "com.carelesscoyotes.remotedata"
version = "0.2"

android {
    compileSdk = 33

    namespace = "remotedata.androidlayout"

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
    jvmToolchain(11)
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api(project(":remotedata"))

    testImplementation(libs.testng)
    testImplementation(libs.mockk)
}

tasks.withType<Test> {
    useTestNG()
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
