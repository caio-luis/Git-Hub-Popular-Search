package com.caioluis.data.remote.implementation

import com.caioluis.data.local.LocalSource
import com.caioluis.data.mappers.toDomain
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository

class GitHubReposRepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource,
) : GitHubReposRepository {

    override suspend fun getGitHubRepositories(
        page: Int,
        language: String,
    ): List<DomainGitHubRepository>? {
        var exception: Throwable? = null

        val remoteResult = try {
            remoteSource.fetchFromRemote(page, language)
        } catch (e: Exception) {
            exception = e
            null
        }

        return remoteResult
            ?.mapNotNull { it?.toDomain(page) }
            ?.takeIf { it.isNotEmpty() }
            ?.let { localSource.saveAndGetFromCache(it, page) }
            ?: run { localSource.getFromCache(page) }?.takeIf { it.isNotEmpty() }
            ?: exception?.let { throw it }
    }
}
