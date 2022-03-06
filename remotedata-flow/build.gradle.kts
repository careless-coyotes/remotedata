plugins {
    kotlin("jvm")
}

group = "remotedata"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.coroutines.core)
    api(project(":remotedata"))

    testImplementation(libs.testng)
    testImplementation("com.google.truth:truth:1.1.3")
}

tasks.withType<Test> {
    useTestNG()
}
