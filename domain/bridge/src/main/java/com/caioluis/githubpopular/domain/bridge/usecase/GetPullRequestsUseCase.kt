package com.caioluis.githubpopular.domain.bridge.usecase

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest

interface GetPullRequestsUseCase {
    suspend fun loadPullRequests(
        page: Int,
        pullUrl: String,
        repositoryId: Int,
    ): List<DomainGitHubPullRequest>
}
