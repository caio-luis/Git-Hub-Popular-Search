package com.caioluis.githubpopular.githubpulls.model

import androidx.compose.runtime.Immutable

@Immutable
data class UiGitHubPullRequest(
    val id: Long = 0,
    val title: String = "",
    val body: String = "",
    val htmlUrl: String = "",
    val userName: String = "",
    val avatarUrl: String = "",
)
