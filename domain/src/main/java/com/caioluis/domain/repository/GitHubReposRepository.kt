package com.caioluis.domain.repository

import com.caioluis.domain.entity.DomainGitHubRepository

/**
 * Created by Caio Luis (@caio.luis) on 11/10/20
 */
interface GitHubReposRepository {
    suspend fun getGitHubRepositories(page: Int): List<DomainGitHubRepository>
    suspend fun deleteRepositories()
}