package com.caioluis.githubpopular.data.pub.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteGitHubRepositories(
    @Json(name = "items")
    val repositories: List<RemoteGitHubRepository?>? = null,
)
