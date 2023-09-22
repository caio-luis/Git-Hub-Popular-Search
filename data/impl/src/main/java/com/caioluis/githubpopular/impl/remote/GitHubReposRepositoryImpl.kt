package com.caioluis.githubpopular.impl.remote

import com.caioluis.githubpopular.data.bridge.local.LocalSource
import com.caioluis.githubpopular.data.bridge.mappers.toDomain
import com.caioluis.githubpopular.data.bridge.remote.NoMoreItemsException
import com.caioluis.githubpopular.data.bridge.remote.RemoteSource
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
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
