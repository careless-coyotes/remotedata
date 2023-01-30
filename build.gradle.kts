import kotlinx.kover.KoverPlugin
import kotlinx.kover.api.CounterType
import kotlinx.kover.api.VerificationTarget
import kotlinx.kover.api.VerificationValueType

plugins {
    base
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.dokka)
    `maven-publish`
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.android.gradle)
    }
}

repositories {
    mavenCentral()
}

koverMerged {
    enable()

    verify {
        rule {
            target = VerificationTarget.ALL

            bound {
                minValue = 99
                counter = CounterType.LINE
                valueType = VerificationValueType.COVERED_PERCENTAGE
            }
        }
    }

    htmlReport {
        onCheck.set(true)
        filters {
            classes {
                excludes += listOf(
                    "remotedata.androidlayout.BuildConfig",
                    "remotedata.compose.BuildConfig",
                    "remotedata.compose.ComposableSingletons*",
                    "remotedata.compose.RemoteDataViewTestKt*",
                )
            }
        }
    }
}

allprojects {
    apply<KoverPlugin>()
}

val release = hasProperty("release")

subprojects {
    afterEvaluate {
        if (!release) version = "$version-SNAPSHOT"
    }
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

subprojects {
    val dokkaJar = tasks.create<Jar>("dokkaJar") {
        archiveClassifier.set("javadoc")
        from(tasks.dokkaHtml)
    }
    plugins.withType<MavenPublishPlugin> {
        publishing {
            repositories {
                maven(
                    if (release) "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                    else "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                ) {
                    name = "MavenCentral"
                    credentials {
                        username = findProperty("ossrh.username").toString()
                        password = findProperty("ossrh.password").toString()
                    }
                }
            }
            publications {
                withType<MavenPublication> {
                    artifact(dokkaJar)
                    pom {
                        name.set(project.name)
                        description.set("Remote request model library for Kotlin.")
                        url.set("https://github.com/careless-coyotes/remotedata")
                        licenses {
                            license {
                                name.set("MIT License")
                                url.set("https://opensource.org/licenses/MIT")
                            }
                        }
                        scm {
                            url.set("https://github.com/careless-coyotes/remotedata")
                        }
                        developers {
                            developer {
                                name.set("Careless Coyotes")
                            }
                        }
                    }
                }
            }
        }
    }
}
