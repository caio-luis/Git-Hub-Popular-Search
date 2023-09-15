package com.caioluis.githubpopular.data.remote.implementation

import com.caioluis.githubpopular.data.remote.model.RemoteGitHubRepository

interface RemoteSource {
    suspend fun fetchFromRemote(
        page: Int,
        language: String,
    ): List<RemoteGitHubRepository?>?
}
