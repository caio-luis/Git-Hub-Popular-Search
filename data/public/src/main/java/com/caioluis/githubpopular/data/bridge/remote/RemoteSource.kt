package com.caioluis.githubpopular.data.bridge.remote

import com.caioluis.githubpopular.data.bridge.remote.model.RemoteGitHubRepository

interface RemoteSource {
    suspend fun fetchFromRemote(
        page: Int,
        language: String,
    ): List<RemoteGitHubRepository?>?
}
