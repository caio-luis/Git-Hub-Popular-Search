package com.caioluis.githubpopular.data.pub.local

import com.caioluis.githubpopular.domain.pub.entity.DomainGitHubRepository

interface LocalSource {
    suspend fun saveToLocalCache(
        repositories: List<DomainGitHubRepository>,
        page: Int,
        language: String
    )

    suspend fun getFromCache(page: Int, language: String): List<DomainGitHubRepository>?
}