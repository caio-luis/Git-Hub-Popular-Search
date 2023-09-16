package com.caioluis.githubpopular.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Context.showShortToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.openBrowserIntent(url: String) {
    val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))

    runCatching {
        this.startActivity(intent)
    }.onFailure {
        showLongToast(it.message.toString())
    }
}
