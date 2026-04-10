package com.caioluis.githubpopular.fixtures

import com.caioluis.githubpopular.githubpulls.model.UiGitHubPullRequest
import com.caioluis.githubpopular.githubrepos.model.UiGitHubRepo

val sampleRepositories = listOf(
    UiGitHubRepo(
        id = 1,
        title = "kotlin",
        description = "The Kotlin Programming Language",
        stargazersCount = 50000,
        forksCount = 6500,
        userName = "JetBrains",
        avatarUrl = "",
        pullsUrl = "https://api.github.com/repos/JetBrains/kotlin/pulls",
        repositoryUrl = "https://github.com/JetBrains/kotlin",
    ),
    UiGitHubRepo(
        id = 2,
        title = "okhttp",
        description = "Square's meticulous HTTP client for the JVM",
        stargazersCount = 45000,
        forksCount = 9200,
        userName = "square",
        avatarUrl = "",
        pullsUrl = "https://api.github.com/repos/square/okhttp/pulls",
        repositoryUrl = "https://github.com/square/okhttp",
    ),
)

val samplePullRequests = listOf(
    UiGitHubPullRequest(
        id = 1L,
        title = "Fix memory leak in coroutine scope",
        body = "This PR fixes a memory leak caused by not cancelling the coroutine scope on destroy.",
        htmlUrl = "https://github.com/JetBrains/kotlin/pull/1",
        userName = "JetBrains",
        avatarUrl = "",
    ),
    UiGitHubPullRequest(
        id = 2L,
        title = "Add support for Kotlin 2.0",
        body = "Adds full support for Kotlin 2.0 features including new compiler plugins.",
        htmlUrl = "https://github.com/square/okhttp/pull/2",
        userName = "square",
        avatarUrl = "",
    ),
)
