package com.caioluis.githubpopular.domain.bridge.repository

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import kotlinx.coroutines.flow.Flow

interface GitHubReposRepository {
    suspend fun getGitHubRepositories(
        page: Int,
        language: String
    ): Flow<List<DomainGitHubRepository>?>
}
