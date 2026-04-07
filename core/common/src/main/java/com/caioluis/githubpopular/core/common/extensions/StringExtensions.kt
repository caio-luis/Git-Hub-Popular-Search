package com.caioluis.githubpopular.core.common.extensions

fun String.truncate(limit: Int): String = if (length >= limit) {
    "${take(limit)}..."
} else {
    this
}
