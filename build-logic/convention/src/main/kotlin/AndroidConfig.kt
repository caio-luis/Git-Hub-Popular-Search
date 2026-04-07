import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroidApplication(
    applicationExtension: ApplicationExtension
) {
    applicationExtension.apply {
        compileSdk = BuildConstants.Android.COMPILE_SDK_VERSION

        defaultConfig {
            targetSdk = BuildConstants.Android.TARGET_SDK_VERSION
            minSdk = BuildConstants.Android.MIN_SDK_VERSION
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            vectorDrawables {
                useSupportLibrary = true
            }
        }

        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }

        compileOptions {
            sourceCompatibility = BuildConstants.Java.VERSION
            targetCompatibility = BuildConstants.Java.VERSION
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(BuildConstants.Java.JVM_TARGET)
        }
    }
}

internal fun Project.configureAndroidLibrary(
    libraryExtension: LibraryExtension
) {
    libraryExtension.apply {
        compileSdk = BuildConstants.Android.COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = BuildConstants.Android.MIN_SDK_VERSION
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            vectorDrawables {
                useSupportLibrary = true
            }
        }

        testOptions {
            unitTests {
                isIncludeAndroidResources = true
                targetSdk = BuildConstants.Android.TARGET_SDK_VERSION
            }
        }

        compileOptions {
            sourceCompatibility = BuildConstants.Java.VERSION
            targetCompatibility = BuildConstants.Java.VERSION
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(BuildConstants.Java.JVM_TARGET)
        }
    }
}
