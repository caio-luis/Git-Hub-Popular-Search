package com.caioluis.githubpopular.domain.impl.usecases

import com.caioluis.githubpopular.domain.pub.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.pub.repository.GitHubReposRepository
import com.caioluis.githubpopular.domain.pub.usecase.ActualPage.pageNumber
import com.caioluis.githubpopular.domain.pub.usecase.GetRepositoriesUseCase
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
