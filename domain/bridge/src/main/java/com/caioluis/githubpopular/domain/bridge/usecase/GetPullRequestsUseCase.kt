package com.caioluis.githubpopular.domain.bridge.usecase

import androidx.paging.PagingData
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import kotlinx.coroutines.flow.Flow

interface GetPullRequestsUseCase {
    fun loadPullRequests(
        pullUrl: String,
        repositoryId: Int,
    ): Flow<PagingData<DomainGitHubPullRequest>>
}
