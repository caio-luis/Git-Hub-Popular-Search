package com.caioluis.githubpopular.data.impl.remote.githubpulls.source

import com.caioluis.githubpopular.data.bridge.remote.githubpulls.model.RemotePullRequest

interface PullRequestsRemoteSource {
    suspend fun fetchPullRequests(url: String, page: Int): List<RemotePullRequest>
}
