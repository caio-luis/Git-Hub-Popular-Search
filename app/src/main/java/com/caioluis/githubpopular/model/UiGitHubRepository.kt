package com.caioluis.githubpopular.model

import com.caioluis.githubpopular.Constants.REPOSITORIES_VIEW_TYPE

data class UiGitHubRepository(
    val id: Int = 0,
    val name: String = "",
    val fullName: String = "",
    val owner: UiRepositoryOwner = UiRepositoryOwner(),
    val description: String = "",
    val pullsUrl: String = "",
    val stargazersCount: Int = 0,
    val forksCount: Int = 0,
    val htmlUrl: String = "",
    override val viewType: Int = REPOSITORIES_VIEW_TYPE
) : UiModel
