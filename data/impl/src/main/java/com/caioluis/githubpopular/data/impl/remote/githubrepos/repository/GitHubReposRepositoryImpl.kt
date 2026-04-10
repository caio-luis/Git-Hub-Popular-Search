package com.caioluis.githubpopular.data.impl.remote.githubrepos.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.caioluis.githubpopular.data.impl.local.githubrepos.mapper.LocalGitHubRepositoryMapper
import com.caioluis.githubpopular.data.impl.local.githubrepos.source.GithubReposLocalSource
import com.caioluis.githubpopular.data.impl.remote.githubrepos.mediator.GithubReposRemoteMediatorFactory
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GitHubReposRepositoryImpl @Inject constructor(
    private val localSource: GithubReposLocalSource,
    private val remoteMediatorFactory: GithubReposRemoteMediatorFactory,
    private val localGitHubRepositoryMapper: LocalGitHubRepositoryMapper,
) : GitHubReposRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getGitHubRepositories(language: String): Flow<PagingData<DomainGitHubRepository>> {
        val pagingSourceFactory = { localSource.getPagedRepositories(language) }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = true,
            ),
            remoteMediator = remoteMediatorFactory.create(language),
            pagingSourceFactory = pagingSourceFactory,
        ).flow.map { pagingData ->
            pagingData.map(localGitHubRepositoryMapper::mapToDomain)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 3
    }
}
