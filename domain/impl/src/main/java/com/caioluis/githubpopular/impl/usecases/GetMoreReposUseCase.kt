package com.caioluis.githubpopular.impl.usecases

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import kotlinx.coroutines.flow.single

class GetMoreReposUseCase(
    private val gitHubReposRepository: GitHubReposRepository,
) {
    suspend fun loadRepositories(
        language: String,
    ): Result<List<DomainGitHubRepository>?> {

        return runCatching {
            gitHubReposRepository.getGitHubRepositories(
                page = ActualPage.pageNumber,
                language = language
            ).single()
        }
    }
}
