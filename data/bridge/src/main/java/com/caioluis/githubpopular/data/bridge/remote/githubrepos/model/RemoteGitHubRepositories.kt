package com.caioluis.githubpopular.data.bridge.remote.githubrepos.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteGitHubRepositories(
    @SerialName("items")
    val repositories: List<RemoteGitHubRepository?>? = null,
)
