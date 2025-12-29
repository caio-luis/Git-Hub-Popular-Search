package com.caioluis.githubpopular.data.impl.remote

import com.caioluis.githubpopular.data.bridge.remote.model.RemotePullRequest
import com.caioluis.githubpopular.data.impl.remote.service.GitHubPullRequestsService
import javax.inject.Inject

class PullRequestsRemoteSourceImpl
@Inject
constructor(
    private val service: GitHubPullRequestsService,
) : PullRequestsRemoteSource {
    override suspend fun fetchPullRequests(url: String): List<RemotePullRequest> = service.getPullRequests(url)
}
