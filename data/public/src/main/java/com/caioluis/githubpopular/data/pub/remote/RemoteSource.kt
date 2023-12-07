package com.caioluis.githubpopular.data.pub.remote

import com.caioluis.githubpopular.data.pub.remote.model.RemoteGitHubRepository

interface RemoteSource {
    suspend fun fetchFromRemote(
        page: Int,
        language: String,
    ): List<RemoteGitHubRepository?>?
}
