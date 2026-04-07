package com.caioluis.githubpopular.githubrepos.model

data class UiGitHubRepo(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val pullsUrl: String = "",
    val stargazersCount: Int = 0,
    val forksCount: Int = 0,
    val repositoryUrl: String = "",
    val userName: String = "",
    val avatarUrl: String = "",
)
