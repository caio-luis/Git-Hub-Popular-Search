package com.caioluis.githubpopular.domain.bridge.repository

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository

interface GitHubReposRepository {
    suspend fun getGitHubRepositories(page: Int, language: String): List<DomainGitHubRepository>?
}
