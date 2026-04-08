package com.caioluis.githubpopular.data.impl.remote.githubrepos

import com.caioluis.githubpopular.data.bridge.remote.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.impl.remote.githubrepos.service.GitHubRepositoriesService
import javax.inject.Inject

class GithubReposRemoteSourceImpl
@Inject
constructor(
    private val gitHubRepositoriesService: GitHubRepositoriesService,
) : GithubReposRemoteSource {
    override suspend fun fetchFromRemote(
        page: Int,
        language: String,
    ): List<RemoteGitHubRepository?>? = gitHubRepositoriesService
        .getGitHubRepositories(page = page, language = language)
        ?.repositories
}
