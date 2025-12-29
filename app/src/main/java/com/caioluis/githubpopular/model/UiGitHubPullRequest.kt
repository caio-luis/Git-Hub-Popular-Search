package com.caioluis.githubpopular.model

data class UiGitHubPullRequest(
    val id: Long = 0,
    val title: String = "",
    val body: String = "",
    val htmlUrl: String = "",
    val userName: String = "",
    val avatarUrl: String = "",
)
