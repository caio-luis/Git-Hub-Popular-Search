package com.caioluis.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteGitHubRepositories(
    @SerializedName("items")
    val repositories: List<RemoteGitHubRepository?>? = null,
)
