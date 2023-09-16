package com.caioluis.githubpopular.domain.impl.usecases

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import com.caioluis.githubpopular.domain.bridge.usecase.ActualPage.pageNumber
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class GetRepositoriesUseCaseImpl(
    private val gitHubReposRepository: GitHubReposRepository,
) : GetRepositoriesUseCase {
    override suspend fun loadRepositories(language: String): Flow<List<DomainGitHubRepository>?> {
        return gitHubReposRepository
            .getGitHubRepositories(pageNumber, language)
            .distinctUntilChanged()
    }
}
