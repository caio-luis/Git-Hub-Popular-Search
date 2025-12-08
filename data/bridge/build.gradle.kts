plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp.plugin)
}

android {
    namespace = "com.caioluis.githubpopular.data.bridge"
    compileSdk = 36

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildFeatures.buildConfig = true

        buildConfigField("String", "API_BASE_URL", "\"https://api.github.com/\"")
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    viewBinding {
        enable = true
    }
}

dependencies {
    api(project(":domain:bridge"))
    runtimeOnly(libs.room.runtime)

    implementation(libs.square.retrofit)
    implementation(libs.square.moshi)
    implementation(libs.room.common)

    ksp(libs.square.moshi.codegen)
    ksp(libs.room.compiler)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.coroutines.test)
}
