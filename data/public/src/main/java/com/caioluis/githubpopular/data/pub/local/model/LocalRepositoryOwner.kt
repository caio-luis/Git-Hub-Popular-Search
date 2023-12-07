package com.caioluis.githubpopular.data.pub.local.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocalRepositoryOwner(
    val id: Int,
    val login: String,
    val avatarUrl: String,
)
