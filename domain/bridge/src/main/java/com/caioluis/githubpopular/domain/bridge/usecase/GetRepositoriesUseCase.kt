package com.caioluis.githubpopular.domain.bridge.usecase

import androidx.paging.PagingData
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import kotlinx.coroutines.flow.Flow

interface GetRepositoriesUseCase {
    fun loadRepositories(language: String): Flow<PagingData<DomainGitHubRepository>>
}
