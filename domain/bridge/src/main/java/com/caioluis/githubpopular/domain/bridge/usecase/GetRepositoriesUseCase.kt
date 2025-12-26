package com.caioluis.githubpopular.domain.bridge.usecase

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import kotlinx.coroutines.flow.Flow

interface GetRepositoriesUseCase {
    suspend fun loadRepositories(page: Int, language: String): Flow<List<DomainGitHubRepository>?>
}
