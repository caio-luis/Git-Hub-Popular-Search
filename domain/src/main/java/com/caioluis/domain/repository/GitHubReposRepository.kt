package com.caioluis.domain.repository

import com.caioluis.domain.entity.DomainGitHubRepository

interface GitHubReposRepository {
    suspend fun getGitHubRepositories(page: Int): List<DomainGitHubRepository>?
}
