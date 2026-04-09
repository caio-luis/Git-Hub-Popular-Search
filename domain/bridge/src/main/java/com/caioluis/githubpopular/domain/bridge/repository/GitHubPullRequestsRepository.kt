package com.caioluis.githubpopular.domain.bridge.repository

import androidx.paging.PagingData
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import kotlinx.coroutines.flow.Flow

interface GitHubPullRequestsRepository {
    fun getPullRequests(
        pullUrl: String,
        repositoryId: Int,
    ): Flow<PagingData<DomainGitHubPullRequest>>
}
