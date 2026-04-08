package com.caioluis.githubpopular.data.impl.local.githubpulls

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest

interface PullRequestsLocalSource {
    suspend fun saveToLocalCache(
        pullRequests: List<DomainGitHubPullRequest>,
        repositoryId: Int,
    )

    suspend fun getFromCache(
        repositoryId: Int,
    ): List<DomainGitHubPullRequest>
}
