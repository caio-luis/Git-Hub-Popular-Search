package com.caioluis.githubpopular.impl.local

import com.caioluis.githubpopular.data.pub.local.LocalSource
import com.caioluis.githubpopular.data.pub.local.dao.GitHubRepositoriesDao
import com.caioluis.githubpopular.data.pub.mappers.toDomain
import com.caioluis.githubpopular.data.pub.mappers.toLocal
import com.caioluis.githubpopular.domain.pub.entity.DomainGitHubRepository

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
