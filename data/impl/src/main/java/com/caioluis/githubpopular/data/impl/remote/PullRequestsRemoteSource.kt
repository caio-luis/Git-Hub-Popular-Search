package com.caioluis.githubpopular.data.impl.remote

import com.caioluis.githubpopular.data.bridge.remote.model.RemotePullRequest

interface PullRequestsRemoteSource {
    suspend fun fetchPullRequests(url: String): List<RemotePullRequest>
}
