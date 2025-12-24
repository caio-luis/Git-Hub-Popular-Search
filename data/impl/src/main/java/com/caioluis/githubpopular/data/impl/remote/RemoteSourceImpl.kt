package com.caioluis.githubpopular.data.impl.remote

import com.caioluis.githubpopular.data.bridge.remote.RemoteSource
import com.caioluis.githubpopular.data.bridge.remote.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.service.GitHubRepositoriesService
import javax.inject.Inject

class RemoteSourceImpl
@Inject
constructor(
    private val gitHubRepositoriesService: GitHubRepositoriesService,
) : RemoteSource {
    override suspend fun fetchFromRemote(
        page: Int,
        language: String,
    ): List<RemoteGitHubRepository?>? = gitHubRepositoriesService
        .getGitHubRepositories(page = page, language = language)
        ?.repositories
}
