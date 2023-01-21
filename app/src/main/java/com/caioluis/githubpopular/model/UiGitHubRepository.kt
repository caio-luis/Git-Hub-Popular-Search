package com.caioluis.githubpopular.model

data class UiGitHubRepository(
        val id: Int = 0,
        val name: String = "",
        val fullName: String = "",
        val owner: UiRepositoryOwner = UiRepositoryOwner(),
        val description: String = "",
        val pullsUrl: String = "",
        val stargazersCount: Int = 0,
        val forksCount: Int = 0,
)
