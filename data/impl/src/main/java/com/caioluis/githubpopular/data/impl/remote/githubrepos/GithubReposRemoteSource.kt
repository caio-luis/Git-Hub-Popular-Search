package com.caioluis.githubpopular.data.impl.remote.githubrepos

import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteGitHubRepository

interface GithubReposRemoteSource {
    suspend fun fetchFromRemote(
        page: Int,
        language: String,
    ): List<RemoteGitHubRepository>
}
