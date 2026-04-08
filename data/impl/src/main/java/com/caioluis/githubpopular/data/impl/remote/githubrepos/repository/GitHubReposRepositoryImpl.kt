package com.caioluis.githubpopular.data.impl.remote.githubrepos.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.caioluis.githubpopular.data.bridge.mappers.toDomain
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.remote.githubrepos.GithubReposRemoteMediatorFactory
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GitHubReposRepositoryImpl @Inject constructor(
    private val localDatabase: GitHubReposDataBase,
    private val remoteMediatorFactory: GithubReposRemoteMediatorFactory,
) : GitHubReposRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getGitHubRepositories(language: String): Flow<PagingData<DomainGitHubRepository>> {
        val pagingSourceFactory = { localDatabase.gitHubRepositoriesDao().getPagedRepositories() }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 3,
                enablePlaceholders = true,
            ),
            remoteMediator = remoteMediatorFactory.create(language),
            pagingSourceFactory = pagingSourceFactory,
        ).flow.map { pagingData ->
            pagingData.map { localRepo ->
                localRepo.toDomain()
            }
        }
    }
}
