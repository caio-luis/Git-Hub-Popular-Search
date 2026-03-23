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
    testImplementation(libs.mockk)
    testImplementation(libs.junit)
}
