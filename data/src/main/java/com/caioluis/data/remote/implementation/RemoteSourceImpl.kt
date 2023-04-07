package com.caioluis.data.remote.implementation

import com.caioluis.data.remote.model.RemoteGitHubRepository
import com.caioluis.data.remote.service.GitHubRepositoriesService

class RemoteSourceImpl(
    private val gitHubRepositoriesService: GitHubRepositoriesService
) : RemoteSource {

    override suspend fun fetchFromRemote(
        page: Int,
        language: String,
    ): List<RemoteGitHubRepository?>? {
        return gitHubRepositoriesService
            .getGitHubRepositories(page = page, language = language)
            ?.repositories
    }
}
