package com.caioluis.githubpopular.domain.bridge.usecase

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository

interface GetRepositoriesUseCase {
    suspend fun loadRepositories(language: String): Result<List<DomainGitHubRepository>?>
}
