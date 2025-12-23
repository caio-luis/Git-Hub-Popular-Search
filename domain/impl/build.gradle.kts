plugins {
    id("githubpopular.jvm.library")
}

dependencies {
    api(project(":domain:bridge"))

    implementation(libs.kotlinx.coroutines)
    implementation(libs.koin.core)

    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.dsl)
}
