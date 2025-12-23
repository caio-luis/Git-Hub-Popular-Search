package com.caioluis.githubpopular.data.bridge.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteRepositoryOwner(
    @param:Json(name = "login")
    val login: String? = "",
    @param:Json(name = "id")
    val id: Int? = null,
    @param:Json(name = "avatar_url")
    val avatarUrl: String? = "",
)
