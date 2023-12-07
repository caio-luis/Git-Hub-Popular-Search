package com.caioluis.githubpopular.domain.pub.repository

import com.caioluis.githubpopular.domain.pub.entity.DomainGitHubRepository
import kotlinx.coroutines.flow.Flow

interface GitHubReposRepository {
    suspend fun getGitHubRepositories(
        page: Int,
        language: String
    ): Flow<List<DomainGitHubRepository>?>
}
