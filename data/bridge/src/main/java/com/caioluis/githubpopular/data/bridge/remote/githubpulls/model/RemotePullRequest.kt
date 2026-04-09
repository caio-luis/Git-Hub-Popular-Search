package com.caioluis.githubpopular.data.bridge.remote.githubpulls.model

import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteRepositoryOwner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemotePullRequest(
    @SerialName("html_url")
    val url: String? = "",
    @SerialName("id")
    val id: Long? = null,
    @SerialName("title")
    val title: String? = "",
    @SerialName("user")
    val user: RemoteRepositoryOwner? = null,
    @SerialName("body")
    val body: String? = "",
)
