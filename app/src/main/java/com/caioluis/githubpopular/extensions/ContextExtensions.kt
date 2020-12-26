package com.caioluis.githubpopular.extensions

import android.content.Context
import android.widget.Toast

/**
 * Created by Caio Luis (@caio.luis) on 28/11/20
 */

fun Context.showShortToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}