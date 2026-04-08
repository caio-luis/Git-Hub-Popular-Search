package com.caioluis.githubpopular.data.impl.remote.githubpullrequests.service

import com.caioluis.githubpopular.data.bridge.remote.model.RemotePullRequest
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GitHubPullRequestsService {
    @GET
    suspend fun getPullRequests(
        @Url url: String,
        @Query("page") page: Int,
    ): List<RemotePullRequest>
}
