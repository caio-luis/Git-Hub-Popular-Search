package com.caioluis.githubpopular.domain.bridge.entity

data class DomainGitHubPullRequest(
    val id: Int = 0,
    val htmlUrl: String = "",
    val title: String = "",
    val body: String = "",
    val userName: String = "",
    val avatarUrl: String = "",
)
