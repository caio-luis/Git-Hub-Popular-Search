plugins {
    id("githubpopular.android.application")
    alias(libs.plugins.ksp.plugin)
}

android {
    namespace = "com.caioluis.githubpopular"

    defaultConfig {
        buildFeatures.buildConfig = true
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