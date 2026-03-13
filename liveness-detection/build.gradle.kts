plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}

android {
    namespace = "com.arifilham.liveness_detection"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release")
    }

//    sourceSets {
//        getByName("main") {
//            assets.srcDirs("src/main/assets")
//        }
//    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.camera:camera-camera2:1.5.3")
    implementation("androidx.camera:camera-lifecycle:1.5.3")
    implementation("androidx.camera:camera-view:1.5.3")
    implementation("com.google.mediapipe:tasks-vision:0.20230731")
}

afterEvaluate {
    extensions.configure<PublishingExtension> {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.ArifIlham0"
                artifactId = "liveness-detection"
                version = "1.0.0"
            }
        }
    }
}