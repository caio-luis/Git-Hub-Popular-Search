package com.caioluis.githubpopular.domain.impl.usecases

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.repository.GitHubPullRequestsRepository
import com.caioluis.githubpopular.domain.bridge.usecase.GetPullRequestsUseCase
import javax.inject.Inject

class GetPullRequestsUseCaseImpl
@Inject
constructor(
    private val gitHubPullRequestsRepository: GitHubPullRequestsRepository,
) : GetPullRequestsUseCase {
    override suspend fun loadPullRequests(
        page: Int,
        pullUrl: String,
        repositoryId: Int,
    ): List<DomainGitHubPullRequest> = gitHubPullRequestsRepository
        .getPullRequests(pullUrl = pullUrl, repositoryId = repositoryId, page = page)
}
