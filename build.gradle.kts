import kotlinx.kover.KoverPlugin
import kotlinx.kover.api.CounterType
import kotlinx.kover.api.VerificationTarget
import kotlinx.kover.api.VerificationValueType

plugins {
    base
    alias(libs.plugins.kotlinx.kover)
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
