package com.caioluis.data.local

import com.caioluis.data.mappers.toDomain
import com.caioluis.data.mappers.toLocal
import com.caioluis.domain.entity.DomainGitHubRepository

class LocalSourceImpl(
    private val gitHubRepositoriesDao: GitHubRepositoriesDao
) : LocalSource {
    override suspend fun saveAndGetFromCache(
        repositories: List<DomainGitHubRepository>,
        page: Int,
    ): List<DomainGitHubRepository>? {
        if (page <= 1)
            gitHubRepositoriesDao.deleteAllGitHubRepositories()
        gitHubRepositoriesDao.saveRepositories(repositories.map { it.toLocal() })
        return getFromCache(page)
    }

    override suspend fun getFromCache(page: Int): List<DomainGitHubRepository>? {
        return gitHubRepositoriesDao.getAllRepositories(page)?.map { it.toDomain() }
    }
}
