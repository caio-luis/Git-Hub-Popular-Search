package com.caioluis.data.remote.implementation

import com.caioluis.data.local.GitHubRepositoriesDao
import com.caioluis.data.mappers.toDomain
import com.caioluis.data.mappers.toLocal
import com.caioluis.data.remote.model.RemoteGitHubRepository
import com.caioluis.data.remote.service.GitHubRepositoriesService
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.repository.GitHubReposRepository
import java.lang.Exception

/**
 * Created by Caio Luis (@caio.luis) on 11/10/20
 */

class GitHubRepositoriesImpl(
    private val gitHubRepositoriesService: GitHubRepositoriesService,
    private val gitHubRepositoriesDao: GitHubRepositoriesDao
) : GitHubReposRepository {

    override suspend fun getGitHubRepositories(page: Int): List<DomainGitHubRepository> {

        var fetchError: Exception? = null

        val repositories = try {
            fetchFromRemote(page).map { it.toDomain() }
        } catch (exception: Exception){
            fetchError = exception
            gitHubRepositoriesDao.getAllRepositories().map { it.toDomain() }
        }

        if (!repositories.isNullOrEmpty()) {
            saveOnLocalCache(repositories)
        } else if (fetchError != null) {
            throw fetchError
        }

        return repositories
    }

    private suspend fun saveOnLocalCache(repositories: List<DomainGitHubRepository>) {
        gitHubRepositoriesDao.deleteAllGitHubRepositories()
        gitHubRepositoriesDao.saveRepositories(repositories.map { it.toLocal() })
    }

    private suspend fun fetchFromRemote(page: Int): List<RemoteGitHubRepository> {
        return gitHubRepositoriesService
            .getGitHubRepositories(page = page)
            .repositories
    }
}