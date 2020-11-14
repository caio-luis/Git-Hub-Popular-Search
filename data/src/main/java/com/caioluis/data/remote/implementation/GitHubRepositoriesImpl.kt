package com.caioluis.data.remote.implementation

import com.caioluis.data.mappers.toDomain
import com.caioluis.data.remote.service.GitHubRepositoriesService
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.repository.GitHubReposRepository

/**
 * Created by Caio Luis (@caio.luis) on 11/10/20
 */

class GitHubRepositoriesImpl(
    private val gitHubRepositoriesService: GitHubRepositoriesService
) : GitHubReposRepository {

    override suspend fun getGitHubRepositories(page: Int): List<DomainGitHubRepository> {
        return gitHubRepositoriesService
            .getGitHubRepositories(page = page)
            .repositories
            .map { remoteRepository -> remoteRepository.toDomain() }
    }
}