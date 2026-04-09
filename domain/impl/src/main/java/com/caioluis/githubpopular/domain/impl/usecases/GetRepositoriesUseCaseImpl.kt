package com.caioluis.githubpopular.domain.impl.usecases

import androidx.paging.PagingData
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepositoriesUseCaseImpl @Inject constructor(
    private val gitHubReposRepository: GitHubReposRepository,
) : GetRepositoriesUseCase {
    override fun loadRepositories(
        language: String,
    ): Flow<PagingData<DomainGitHubRepository>> = gitHubReposRepository.getGitHubRepositories(language)
}
