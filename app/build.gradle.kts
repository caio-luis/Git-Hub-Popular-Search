plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp.plugin)
}

android {
    namespace = "com.caioluis.githubpopular"
    compileSdk = 34

    defaultConfig {
        minSdk = 23
        

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildFeatures.buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
    api(project(":data:impl"))
    api(project(":data:bridge"))
    api(project(":domain:impl"))
    api(project(":domain:bridge"))

    implementation(libs.androidx.activity)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.core)
    implementation(libs.kotlin.stdlib)
    implementation(libs.app.compat)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.recycler.view)
    implementation(libs.card.view)
    implementation(libs.swipe.refresh.layout)
    implementation(libs.bumptech.glide)

    ksp(libs.androidx.lifecycle.common)
    ksp(libs.bumptech.glide.compiler)

    testRuntimeOnly(libs.android.test.core)
    testRuntimeOnly(libs.robolectric)
    testRuntimeOnly(libs.android.test.runner)

    testImplementation(libs.mockk)
    testImplementation(libs.android.core.testing)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockk.dsl)
    testImplementation(libs.junit)

    debugImplementation(libs.leakcanary.android)
}