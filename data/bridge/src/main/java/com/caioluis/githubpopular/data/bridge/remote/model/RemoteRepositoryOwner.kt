package com.caioluis.githubpopular.data.bridge.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteRepositoryOwner(
    @SerialName("login")
    val login: String? = "",
    @SerialName("id")
    val id: Int? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = "",
)
