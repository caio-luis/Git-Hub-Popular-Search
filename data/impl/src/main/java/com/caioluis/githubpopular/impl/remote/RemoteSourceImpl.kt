package com.caioluis.githubpopular.impl.remote

import com.caioluis.githubpopular.data.bridge.remote.RemoteSource
import com.caioluis.githubpopular.data.bridge.remote.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.service.GitHubRepositoriesService

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
