package com.caioluis.githubpopular.data.bridge.remote.githubrepos.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteGitHubRepository(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = "",
    @SerialName("full_name")
    val fullName: String? = "",
    @SerialName("owner")
    val owner: RemoteRepositoryOwner? = null,
    @SerialName("description")
    val description: String? = "",
    @SerialName("pulls_url")
    val pullsUrl: String? = "",
    @SerialName("stargazers_count")
    val stargazersCount: Int? = null,
    @SerialName("forks_count")
    val forksCount: Int? = null,
    @SerialName("html_url")
    val htmlUrl: String? = "",
)
