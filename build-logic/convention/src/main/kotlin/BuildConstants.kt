import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object BuildConstants {

    object Android {
        const val COMPILE_SDK_VERSION = 36
        const val MIN_SDK_VERSION = 23
        const val TARGET_SDK_VERSION = 36
    }

    object Java {
        val VERSION: JavaVersion = JavaVersion.VERSION_21
        val JVM_TARGET: JvmTarget = JvmTarget.JVM_21
    }
}