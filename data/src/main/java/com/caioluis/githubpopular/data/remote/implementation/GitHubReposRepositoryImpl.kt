package com.caioluis.githubpopular.data.remote.implementation

import com.caioluis.githubpopular.data.local.LocalSource
import com.caioluis.githubpopular.data.mappers.toDomain
import com.caioluis.githubpopular.data.remote.NoMoreItemsException
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GitHubReposRepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource,
) : GitHubReposRepository {

    override suspend fun getGitHubRepositories(
        page: Int,
        language: String,
    ): Flow<List<DomainGitHubRepository>?> = flow {
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
    }.catch { previousError ->
        emit(
            localSource.getFromCache(page, language)
                ?.takeIf { it.isNotEmpty() }
                ?: throw previousError
        )
    }
}
