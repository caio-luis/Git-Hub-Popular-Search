package com.caioluis.githubpopular.domain.bridge.repository

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest

interface GitHubPullRequestsRepository {
    suspend fun getPullRequests(
        pullUrl: String,
        repositoryId: Int,
    ): List<DomainGitHubPullRequest>
}
