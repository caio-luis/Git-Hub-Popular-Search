package com.caioluis.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteGitHubRepository(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String? = "",

    @SerializedName("full_name")
    val fullName: String? = "",

    @SerializedName("owner")
    val owner: RemoteRepositoryOwner?,

    @SerializedName("description")
    val description: String? = "",

    @SerializedName("pulls_url")
    val pullsUrl: String? = "",

    @SerializedName("stargazers_count")
    val stargazersCount: Int?,

    @SerializedName("forks_count")
    val forksCount: Int?,

    @SerializedName("html_url")
    val htmlUrl: String?,
)
