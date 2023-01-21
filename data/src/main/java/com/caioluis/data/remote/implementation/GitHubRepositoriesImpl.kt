package com.caioluis.data.remote.implementation

import com.caioluis.data.local.GitHubRepositoriesDao
import com.caioluis.data.mappers.toDomain
import com.caioluis.data.mappers.toLocal
import com.caioluis.data.remote.model.RemoteGitHubRepository
import com.caioluis.data.remote.service.GitHubRepositoriesService
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.repository.GitHubReposRepository

class GitHubRepositoriesImpl(
    private val gitHubRepositoriesService: GitHubRepositoriesService,
    private val gitHubRepositoriesDao: GitHubRepositoriesDao,
) : GitHubReposRepository {

    override suspend fun getGitHubRepositories(page: Int): List<DomainGitHubRepository>? {
        var exception: Throwable? = null

        val remoteResult = try {
            fetchFromRemote(page)
        } catch (e: Exception) {
            exception = e
            null
        }

        return remoteResult
            ?.mapNotNull { it?.toDomain(page) }
            ?.takeIf { it.isNotEmpty() }
            ?.let {
                saveOnLocalCache(
                    repositories = it,
                    deletePrevious = page <= 1
                )
            }
            ?: run {
                gitHubRepositoriesDao
                    .getAllRepositories(page)
                    ?.map { it.toDomain() }
            }?.takeIf { it.isNotEmpty() }
            ?: exception?.let { throw it }
    }

    private suspend fun saveOnLocalCache(
        repositories: List<DomainGitHubRepository>,
        deletePrevious: Boolean = false,
    ): List<DomainGitHubRepository> {
        if (deletePrevious)
            gitHubRepositoriesDao.deleteAllGitHubRepositories()
        gitHubRepositoriesDao.saveRepositories(repositories.map { it.toLocal() })

        return repositories
    }

    private suspend fun fetchFromRemote(page: Int): List<RemoteGitHubRepository?>? {
        return gitHubRepositoriesService
            .getGitHubRepositories(page = page)
            ?.repositories
    }
}
