package com.caioluis.githubpopular.domain.bridge.repository

import androidx.paging.PagingData
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import kotlinx.coroutines.flow.Flow

interface GitHubReposRepository {
    fun getGitHubRepositories(language: String): Flow<PagingData<DomainGitHubRepository>>
}
