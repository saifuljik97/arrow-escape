import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.plugin.compose")
    id("jacoco")
}

val versionProps = Properties()
file("version.properties").inputStream().use { versionProps.load(it) }

android {
    namespace = "com.batodev.arrows"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.dtzllc.arrowescape"
        minSdk = 29
        targetSdk = 37
        versionCode = versionProps.getProperty("versionCode").toInt()
        versionName = versionProps.getProperty("versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
        }
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

tasks.withType<Test> {
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

jacoco {
    toolVersion = "0.8.13"
}

configurations.all {
    resolutionStrategy {
        force(libs.androidx.annotation.experimental)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx) { version { strictly("1.17.0") } }
    implementation(libs.androidx.lifecycle.runtime.ktx) { version { strictly("2.10.0") } }
    implementation(libs.androidx.activity.compose) { version { strictly("1.12.4") } }
    implementation(platform(libs.arrows.androidx.compose.bom))
    implementation("androidx.compose.ui:ui")
    implementation(libs.bumble.appyx.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(project(":navigation"))
    implementation(project(":feature:home"))
    implementation(project(":core:ui"))
    implementation(project(":data"))
    implementation(project(":ads"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.espresso.intents)
    androidTestImplementation(platform(libs.arrows.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(project(":core:models"))
    androidTestImplementation(project(":domain"))
    androidTestImplementation(project(":feature:game"))
    debugImplementation(libs.bumble.appyx.testing.ui.activity)
    androidTestImplementation(libs.bumble.appyx.testing.ui)
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
