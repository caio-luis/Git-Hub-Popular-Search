package com.caioluis.data.remote.implementation

import com.caioluis.data.local.GitHubRepositoriesDao
import com.caioluis.data.mappers.toDomain
import com.caioluis.data.mappers.toLocal
import com.caioluis.data.remote.model.RemoteGitHubRepository
import com.caioluis.data.remote.service.GitHubRepositoriesService
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.repository.GitHubReposRepository

/**
 * Created by Caio Luis (@caio.luis) on 11/10/20
 */

class GitHubRepositoriesImpl(
    private val gitHubRepositoriesService: GitHubRepositoriesService,
    private val gitHubRepositoriesDao: GitHubRepositoriesDao
) : GitHubReposRepository {

    override suspend fun getGitHubRepositories(page: Int): List<DomainGitHubRepository> {
        //TODO implement pagination with network and room persistance

        return fetchFromRemote(page).map { it.toDomain() }

//        val repositories = gitHubRepositoriesDao.getAllRepositories().map { it.toDomain() }
//
//        return if (repositories.isEmpty()) {
//            val remoteRepositories = fetchFromRemote(page).map { it.toDomain() }
//            saveOnLocalCache(remoteRepositories)
//            gitHubRepositoriesDao.getAllRepositories().map { it.toDomain() }
//        } else {
//            repositories
//        }
    }

    override suspend fun deleteRepositories() {
        gitHubRepositoriesDao.deleteAllGitHubRepositories()
    }

    private suspend fun saveOnLocalCache(repositories: List<DomainGitHubRepository>) {
        gitHubRepositoriesDao.saveRepositories(repositories.map { it.toLocal() })
    }

    private suspend fun fetchFromRemote(page: Int): List<RemoteGitHubRepository> {
        return gitHubRepositoriesService
            .getGitHubRepositories(page = page)
            .repositories
    }
}