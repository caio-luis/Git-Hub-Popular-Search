package com.caioluis.githubpopular.data.bridge.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteGitHubRepository(
    @param:Json(name = "id")
    val id: Int? = null,
    @param:Json(name = "name")
    val name: String? = "",
    @param:Json(name = "full_name")
    val fullName: String? = "",
    @param:Json(name = "owner")
    val owner: RemoteRepositoryOwner? = null,
    @param:Json(name = "description")
    val description: String? = "",
    @param:Json(name = "pulls_url")
    val pullsUrl: String? = "",
    @param:Json(name = "stargazers_count")
    val stargazersCount: Int? = null,
    @param:Json(name = "forks_count")
    val forksCount: Int? = null,
    @param:Json(name = "html_url")
    val htmlUrl: String? = "",
)
