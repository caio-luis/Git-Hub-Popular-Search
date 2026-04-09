package com.caioluis.githubpopular.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MainDestination

@Serializable
data class PullRequestsDestination(
    val pullUrl: String,
    val repositoryId: Int,
    val repositoryName: String,
)
