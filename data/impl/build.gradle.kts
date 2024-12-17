plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp.plugin)
}

android {
    namespace = "com.caioluis.githubpopular.data.impl"
    compileSdk = 34

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
            isMinifyEnabled = true
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
    api(project(":data:bridge"))

    runtimeOnly(libs.room.runtime)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.room.ktx)
    implementation(libs.square.retrofit)
    implementation(libs.square.retrofit.converter.moshi)
    implementation(libs.square.moshi)
    implementation(libs.square.okhttp)
    implementation(libs.room.common)
    implementation(libs.androidx.sqlite)

    ksp(libs.room.compiler)
    ksp(libs.square.moshi.codegen)
    ksp(libs.koin.android)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.coroutines.test)
}
