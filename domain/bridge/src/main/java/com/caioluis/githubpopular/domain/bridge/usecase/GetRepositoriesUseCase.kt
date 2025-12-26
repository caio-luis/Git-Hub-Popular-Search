package com.caioluis.githubpopular.domain.bridge.usecase

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository

interface GetRepositoriesUseCase {
    suspend fun loadRepositories(page: Int, language: String): List<DomainGitHubRepository>?
}
