package com.caioluis.data.local

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository

interface LocalSource {
    suspend fun saveToLocalCache(
        repositories: List<DomainGitHubRepository>,
        page: Int,
        language: String
    )

    suspend fun getFromCache(page: Int, language: String): List<DomainGitHubRepository>?
}