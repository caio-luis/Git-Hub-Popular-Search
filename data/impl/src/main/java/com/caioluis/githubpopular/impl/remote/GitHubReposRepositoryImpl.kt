package com.caioluis.githubpopular.impl.remote

import com.caioluis.githubpopular.data.pub.local.LocalSource
import com.caioluis.githubpopular.data.pub.mappers.toDomain
import com.caioluis.githubpopular.data.pub.remote.NoMoreItemsException
import com.caioluis.githubpopular.data.pub.remote.RemoteSource
import com.caioluis.githubpopular.domain.pub.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.pub.repository.GitHubReposRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GitHubReposRepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource,
) : GitHubReposRepository {

    override suspend fun getGitHubRepositories(
        page: Int,
        language: String,
    ): Flow<List<DomainGitHubRepository>?> = flow {
        runCatching {
            emit(
                remoteSource.fetchFromRemote(page, language)
                    ?.mapNotNull { it?.toDomain(page, language) }
                    ?.takeIf { it.isNotEmpty() }
                    ?.let {
                        localSource.saveToLocalCache(it, page, language)
                        localSource.getFromCache(page, language)
                    }
                    ?.takeIf { it.isNotEmpty() }
                    ?: throw NoMoreItemsException()
            )
        }.onFailure { previousError ->
            emit(
                localSource.getFromCache(page, language)
                    ?.takeIf { it.isNotEmpty() }
                    ?: throw previousError
            )
        }
    }.flowOn(Dispatchers.IO)
}
