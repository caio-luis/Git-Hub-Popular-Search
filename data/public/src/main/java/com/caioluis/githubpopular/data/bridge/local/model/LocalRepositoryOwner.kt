package com.caioluis.githubpopular.data.bridge.local.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocalRepositoryOwner(
    val id: Int,
    val login: String,
    val avatarUrl: String,
)
