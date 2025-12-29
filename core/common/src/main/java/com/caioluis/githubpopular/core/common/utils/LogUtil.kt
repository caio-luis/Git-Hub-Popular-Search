package com.caioluis.githubpopular.core.common.utils

import android.util.Log
import com.caioluis.githubpopular.core.common.BuildConfig

object LogUtil {

    fun d(tag: String, message: String) = log { Log.d(tag, message) }

    fun e(tag: String?, message: String?, throwable: Throwable? = null) = log {
        Log.e(tag, message, throwable)
    }

    fun i(tag: String?, message: String) = log { Log.i(tag, message) }

    fun w(tag: String, message: String?, throwable: Throwable? = null) = log {
        Log.w(tag, message, throwable)
    }

    private inline fun log(block: () -> Unit) {
        if (BuildConfig.DEBUG) {
            block()
        }
    }
}
