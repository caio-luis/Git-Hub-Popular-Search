package com.caioluis.githubpopular.data.local

import com.caioluis.githubpopular.data.mappers.toDomain
import com.caioluis.githubpopular.data.mappers.toLocal
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository

class LocalSourceImpl(
    private val gitHubRepositoriesDao: GitHubRepositoriesDao
) : LocalSource {
    override suspend fun saveToLocalCache(
        repositories: List<DomainGitHubRepository>,
        page: Int,
        language: String
    ) {
        if (page <= 1)
            gitHubRepositoriesDao.deleteReposByLanguage(language)
        gitHubRepositoriesDao.saveRepositories(repositories.map { it.toLocal() })
    }

    override suspend fun getFromCache(page: Int, language: String): List<DomainGitHubRepository>? {
        return gitHubRepositoriesDao.getAllRepositories(page, language)?.map { it.toDomain() }
    }
}
