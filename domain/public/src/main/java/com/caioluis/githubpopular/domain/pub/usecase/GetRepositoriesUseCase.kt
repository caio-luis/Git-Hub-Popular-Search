package com.caioluis.githubpopular.domain.pub.usecase

import com.caioluis.githubpopular.domain.pub.entity.DomainGitHubRepository
import kotlinx.coroutines.flow.Flow

interface GetRepositoriesUseCase {
    suspend fun loadRepositories(language: String): Flow<List<DomainGitHubRepository>?>
}
