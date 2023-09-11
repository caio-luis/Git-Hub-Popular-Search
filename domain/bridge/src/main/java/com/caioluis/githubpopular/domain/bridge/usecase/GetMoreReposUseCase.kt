package com.caioluis.githubpopular.domain.bridge.usecase

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository

interface GetMoreReposUseCase {
    suspend fun loadRepositories(language: String): Result<List<DomainGitHubRepository>?>
}
