package com.caioluis.githubpopular.domain.impl.usecases

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.domain.impl.usecases.ActualPage.pageNumber
import kotlinx.coroutines.flow.single

class GetRepositoriesUseCaseImpl(
    private val gitHubReposRepository: GitHubReposRepository,
) : GetRepositoriesUseCase {
    override suspend fun loadRepositories(language: String): Result<List<DomainGitHubRepository>?> {
        return runCatching {
            ActualPage.reset()
            gitHubReposRepository
                .getGitHubRepositories(page = pageNumber, language)
                .single()
        }
    }
}
