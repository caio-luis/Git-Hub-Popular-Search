package com.caioluis.githubpopular.data.bridge.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemotePullRequest(
    @SerialName("url")
    val url: String? = "",
    @SerialName("id")
    val id: Int? = null,
    @SerialName("title")
    val title: String? = "",
    @SerialName("user")
    val user: RemoteRepositoryOwner? = null,
    @SerialName("body")
    val body: String? = "",
)
