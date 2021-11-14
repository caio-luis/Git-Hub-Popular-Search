package com.caioluis.data.remote.implementation

import com.caioluis.data.local.GitHubRepositoriesDao
import com.caioluis.data.mappers.toDomain
import com.caioluis.data.mappers.toLocal
import com.caioluis.data.remote.model.RemoteGitHubRepository
import com.caioluis.data.remote.service.GitHubRepositoriesService
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.repository.GitHubReposRepository

/**
 * Created by Caio Luis (caio-luis) on 11/10/20
 */

class GitHubRepositoriesImpl(
    private val gitHubRepositoriesService: GitHubRepositoriesService,
    private val gitHubRepositoriesDao: GitHubRepositoriesDao
) : GitHubReposRepository {

    override suspend fun getGitHubRepositories(page: Int): List<DomainGitHubRepository> {

        var exception: java.lang.Exception? = null

        val remoteRepositories: List<DomainGitHubRepository>? = try {
            fetchFromRemote(page).map { it.toDomain(page) }
        } catch (ex: Exception) {
            exception = ex
            null
        }

        return getReposBasedOnRemoteResponse(remoteRepositories, page, exception)
    }

    private suspend fun getReposBasedOnRemoteResponse(
        remoteRepositories: List<DomainGitHubRepository>?,
        page: Int,
        exception: java.lang.Exception?
    ): List<DomainGitHubRepository> {
        return when {
            remoteRepositories.isNullOrEmpty() -> {
                val localRepositories = gitHubRepositoriesDao.getAllRepositories(page)
                    .map { it.toDomain() }

                if (localRepositories.isNullOrEmpty()) {
                    throw exception!!
                } else {
                    localRepositories
                }
            }
            page <= 1 -> {
                saveOnLocalCache(
                    repositories = remoteRepositories,
                    deletePrevious = true
                )
            }
            else -> {
                saveOnLocalCache(remoteRepositories.orEmpty())
            }
        }
    }

    private suspend fun saveOnLocalCache(
        repositories: List<DomainGitHubRepository>,
        deletePrevious: Boolean = false
    ): List<DomainGitHubRepository> {
        if (deletePrevious)
            deleteRepositories()
        gitHubRepositoriesDao.saveRepositories(repositories.map { it.toLocal() })

        return repositories
    }

    override suspend fun deleteRepositories() {
        gitHubRepositoriesDao.deleteAllGitHubRepositories()
    }

    private suspend fun fetchFromRemote(page: Int): List<RemoteGitHubRepository> {
        return gitHubRepositoriesService
            .getGitHubRepositories(page = page)
            .repositories
    }
}