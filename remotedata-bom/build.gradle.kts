plugins {
    `java-platform`
    `maven-publish`
}

group = "remotedata"
version = "0.1"

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
