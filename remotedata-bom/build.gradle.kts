plugins {
    `java-platform`
    `maven-publish`
    signing
}

group = "com.carelesscoyotes.remotedata"
version = "0.3"

repositories {
    mavenCentral()
}

dependencies {
    constraints {
        rootProject.subprojects.forEach { subproject ->
            if (subproject != project) {
                api(subproject)
            }
        }
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
