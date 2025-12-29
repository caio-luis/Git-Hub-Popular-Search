package com.caioluis.githubpopular.data.impl.remote.service

import com.caioluis.githubpopular.data.bridge.remote.model.RemotePullRequest
import retrofit2.http.GET
import retrofit2.http.Url

interface GitHubPullRequestsService {
    @GET
    suspend fun getPullRequests(
        @Url url: String,
    ): List<RemotePullRequest>
}
