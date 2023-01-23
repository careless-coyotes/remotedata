plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "remotedata"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.rxjava2.rxkotlin)
    api(project(":remotedata"))

    testImplementation(libs.testng)
}

tasks.withType<Test> {
    useTestNG()
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components["kotlin"])
            artifact(tasks.kotlinSourcesJar)
        }
    }
}
