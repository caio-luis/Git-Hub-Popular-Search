package com.caioluis.githubpopular.domain.impl.usecases

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import javax.inject.Inject

class GetRepositoriesUseCaseImpl
@Inject
constructor(
    private val gitHubReposRepository: GitHubReposRepository,
) : GetRepositoriesUseCase {
    override suspend fun loadRepositories(
        page: Int,
        language: String,
    ): List<DomainGitHubRepository> = gitHubReposRepository
        .getGitHubRepositories(page, language)
}
