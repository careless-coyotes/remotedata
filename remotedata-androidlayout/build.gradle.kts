plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
    signing
}

group = "com.carelesscoyotes.remotedata"
version = "0.1"

android {
    compileSdk = 32

    defaultConfig {
        @Suppress("UnstableApiUsage")
        minSdk = 21
        @Suppress("UnstableApiUsage")
        targetSdk = 32

        @Suppress("UnstableApiUsage")
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        @Suppress("UnstableApiUsage")
        sourceCompatibility = JavaVersion.VERSION_1_8
        @Suppress("UnstableApiUsage")
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
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
