package com.caioluis.data.remote.implementation

import com.caioluis.data.remote.model.RemoteGitHubRepository

interface RemoteSource {
    suspend fun fetchFromRemote(
        page: Int,
        language: String,
    ): List<RemoteGitHubRepository?>?
}
