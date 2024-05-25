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
    implementation(libs.rxjava3.rxkotlin)
    api(project(":remotedata"))

    testImplementation(kotlin("test"))
}

kotlin.jvmToolchain(11)

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
