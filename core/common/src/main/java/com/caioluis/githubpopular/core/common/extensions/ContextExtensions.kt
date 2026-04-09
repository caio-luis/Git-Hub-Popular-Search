package com.caioluis.githubpopular.core.common.extensions

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import timber.log.Timber

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.openBrowserIntent(url: String) {
    val intent = Intent(Intent.ACTION_VIEW).setData(url.toUri())

    runCatching {
        this.startActivity(intent)
    }.onFailure {
        Timber.e(it, "Failed to open browser intent: url=%s", url)
        showLongToast(it.message.toString())
    }
}
