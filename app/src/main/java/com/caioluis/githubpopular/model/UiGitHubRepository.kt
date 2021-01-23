package com.caioluis.githubpopular.model

/**
 * Created by Caio Luis (caio-luis) on 12/10/20
 */
data class UiGitHubRepository(
        val id: Int = 0,
        val name: String = "",
        val fullName: String = "",
        val owner: UiRepositoryOwner = UiRepositoryOwner(),
        val description: String = "",
        val pullsUrl: String = "",
        val stargazersCount: Int = 0,
        val forksCount: Int = 0
)
