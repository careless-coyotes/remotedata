plugins {
    kotlin("jvm")
    `maven-publish`
    signing
}

group = "com.carelesscoyotes.remotedata"
version = "0.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.rxjava3.rxkotlin)
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

signing {
    useGpgCmd()
    sign(publishing.publications)
}
