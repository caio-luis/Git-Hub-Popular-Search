package com.caioluis.githubpopular.extensions

fun String.truncate(limit: Int): String = if (length >= limit) {
    "${take(limit)}..."
} else {
    this
}
