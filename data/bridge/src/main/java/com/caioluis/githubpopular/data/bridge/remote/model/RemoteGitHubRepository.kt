package com.caioluis.githubpopular.data.bridge.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteGitHubRepository(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "name")
    val name: String? = "",

    @Json(name = "full_name")
    val fullName: String? = "",

    @Json(name = "owner")
    val owner: RemoteRepositoryOwner? = null,

    @Json(name = "description")
    val description: String? = "",

    @Json(name = "pulls_url")
    val pullsUrl: String? = "",

    @Json(name = "stargazers_count")
    val stargazersCount: Int? = null,

    @Json(name = "forks_count")
    val forksCount: Int? = null,

    @Json(name = "html_url")
    val htmlUrl: String? = ""
)
