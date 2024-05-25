plugins {
    `java-platform`
    `maven-publish`
    signing
}

group = "com.carelesscoyotes.remotedata"
version = "0.5"

repositories {
    mavenCentral()
}

dependencies {
    constraints {
        rootProject.subprojects
            .filterNot(project::equals)
            .forEach(::api)
    }
}

publishing {
    publications {
        create<MavenPublication>("bom") {
            from(components["javaPlatform"])
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
