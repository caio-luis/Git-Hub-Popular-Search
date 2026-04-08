plugins {
    id("githubpopular.android.library")
}

android {
    namespace = "com.caioluis.githubpopular.core.common"

    defaultConfig {
        buildFeatures.buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.annotation.jvm)
    implementation(libs.androidx.core.ktx)
    implementation(libs.square.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.android)
    testImplementation(libs.mockk)
    testImplementation(libs.junit)
}
