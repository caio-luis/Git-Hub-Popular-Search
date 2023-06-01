package com.caioluis.data.local

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository

interface LocalSource {
    suspend fun saveAndGetFromCache(
        repositories: List<DomainGitHubRepository>,
        page: Int,
    ): List<DomainGitHubRepository>?

    suspend fun getFromCache(page: Int): List<DomainGitHubRepository>?
}