package com.caioluis.githubpopular.data.impl.remote.githubpulls.source

import com.caioluis.githubpopular.data.bridge.remote.githubpulls.model.RemotePullRequest
import com.caioluis.githubpopular.data.impl.remote.githubpulls.service.GitHubPullRequestsService
import javax.inject.Inject

class PullRequestsRemoteSourceImpl @Inject constructor(
    private val service: GitHubPullRequestsService,
) : PullRequestsRemoteSource {
    override suspend fun fetchPullRequests(url: String, page: Int): List<RemotePullRequest> = service.getPullRequests(url, page)
}
