package com.caioluis.githubpopular.impl.usecases

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import com.caioluis.githubpopular.domain.bridge.usecase.GetMoreReposUseCase
import kotlinx.coroutines.flow.single

class GetMoreReposUseCaseImpl(
    private val gitHubReposRepository: GitHubReposRepository,
) : GetMoreReposUseCase {
    override suspend fun loadRepositories(language: String): Result<List<DomainGitHubRepository>?> {
        return runCatching {
            gitHubReposRepository.getGitHubRepositories(
                page = ActualPage.pageNumber,
                language = language
            ).single()
        }
    }
}
