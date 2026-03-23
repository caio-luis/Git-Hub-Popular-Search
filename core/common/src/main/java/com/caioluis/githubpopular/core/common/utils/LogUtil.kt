package com.caioluis.githubpopular.core.common.utils

import android.util.Log
import androidx.annotation.Keep
import androidx.annotation.VisibleForTesting
import com.caioluis.githubpopular.core.common.BuildConfig

@Keep
object LogUtil {

    @VisibleForTesting
    var isDebug: Boolean = BuildConfig.DEBUG

    fun d(tag: String, message: String) = log { Log.d(tag, message) }

    fun e(tag: String?, message: String?, throwable: Throwable? = null) = log {
        Log.e(tag, message, throwable)
    }

    fun i(tag: String?, message: String) = log { Log.i(tag, message) }

    fun w(tag: String, message: String?, throwable: Throwable? = null) = log {
        Log.w(tag, message, throwable)
    }

    private inline fun log(block: () -> Unit) {
        if (isDebug) {
            block()
        }
    }
}
