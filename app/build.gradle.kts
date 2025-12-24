plugins {
    id("githubpopular.android.application")
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.caioluis.githubpopular"

    defaultConfig {
        buildFeatures.buildConfig = true
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    api(project(":data:impl"))
    api(project(":data:bridge"))
    api(project(":domain:impl"))
    api(project(":domain:bridge"))

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.runtime.compose)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.core)
    implementation(libs.app.compat)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.hilt.android)

    ksp(libs.androidx.lifecycle.common)
    ksp(libs.bumptech.glide.compiler)
    ksp(libs.hilt.compiler)
    ksp(libs.kotlin.metadata.jvm)

    testRuntimeOnly(libs.android.test.core)
    testRuntimeOnly(libs.robolectric)
    testRuntimeOnly(libs.android.test.runner)

    testImplementation(libs.mockk)
    testImplementation(libs.android.core.testing)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockk.dsl)
    testImplementation(libs.junit)

    debugImplementation(libs.leakcanary.android)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}
