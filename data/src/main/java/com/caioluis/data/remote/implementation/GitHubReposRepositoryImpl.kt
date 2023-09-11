package com.caioluis.data.remote.implementation

import com.caioluis.data.local.LocalSource
import com.caioluis.data.mappers.toDomain
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
                ?.mapNotNull { it?.toDomain(page) }
                ?.takeIf { it.isNotEmpty() }
                ?.let { localSource.saveAndGetFromCache(it, page) }
                ?: run { localSource.getFromCache(page) }
                    ?.takeIf { it.isNotEmpty() }
                ?: throw Exception("Não tem item para carregar!")
        )
    }.catch { previousError ->
        emit(
            localSource.getFromCache(page)
                ?.takeIf { it.isNotEmpty() }
                ?: throw previousError
        )
    }
}
