package com.caioluis.githubpopular.domain.impl.usecases

import androidx.paging.PagingData
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.repository.GitHubPullRequestsRepository
import com.caioluis.githubpopular.domain.bridge.usecase.GetPullRequestsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPullRequestsUseCaseImpl
@Inject
constructor(
    private val gitHubPullRequestsRepository: GitHubPullRequestsRepository,
) : GetPullRequestsUseCase {
    override fun loadPullRequests(
        pullUrl: String,
        repositoryId: Int,
    ): Flow<PagingData<DomainGitHubPullRequest>> = gitHubPullRequestsRepository
        .getPullRequests(pullUrl = pullUrl, repositoryId = repositoryId)
}
