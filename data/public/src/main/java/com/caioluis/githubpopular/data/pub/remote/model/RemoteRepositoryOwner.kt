package com.caioluis.githubpopular.data.pub.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteRepositoryOwner(
    @Json(name = "login")
    val login: String? = "",

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "avatar_url")
    val avatarUrl: String? = "",
)
