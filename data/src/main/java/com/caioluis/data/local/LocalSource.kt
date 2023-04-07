package com.caioluis.data.local

import com.caioluis.domain.entity.DomainGitHubRepository

interface LocalSource {
    suspend fun getFromLocalDatabase(
        repositories: List<DomainGitHubRepository>,
        page: Int,
    ): List<DomainGitHubRepository>?
}