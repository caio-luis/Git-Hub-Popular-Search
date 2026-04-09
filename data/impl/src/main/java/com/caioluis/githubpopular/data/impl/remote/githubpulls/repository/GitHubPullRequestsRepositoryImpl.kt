package com.caioluis.githubpopular.data.impl.remote.githubpulls.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.caioluis.githubpopular.data.impl.local.githubpulls.GithubPullRequestsLocalSource
import com.caioluis.githubpopular.data.impl.mapper.LocalGitHubPullRequestMapper
import com.caioluis.githubpopular.data.impl.remote.githubpulls.mediator.GithubPullRequestsRemoteMediatorFactory
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.repository.GitHubPullRequestsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GitHubPullRequestsRepositoryImpl
@Inject
constructor(
    private val localSource: GithubPullRequestsLocalSource,
    private val remoteMediatorFactory: GithubPullRequestsRemoteMediatorFactory,
    private val localGitHubPullRequestMapper: LocalGitHubPullRequestMapper,
) : GitHubPullRequestsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPullRequests(
        pullUrl: String,
        repositoryId: Int,
    ): Flow<PagingData<DomainGitHubPullRequest>> {
        val pagingSourceFactory = {
            localSource.getPagedPullRequests(repositoryId)
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = true,
            ),
            remoteMediator = remoteMediatorFactory.create(
                pullUrl = pullUrl,
                repositoryId = repositoryId,
            ),
            pagingSourceFactory = pagingSourceFactory,
        ).flow.map { pagingData ->
            pagingData.map(localGitHubPullRequestMapper::mapToDomain)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 3
    }
}
