package com.caioluis.data.remote.service

import com.caioluis.data.remote.model.RemoteGitHubRepositories
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Caio Luis (caio-luis) on 10/10/20
 */
interface GitHubRepositoriesService {

    @GET("search/repositories")
    suspend fun getGitHubRepositories(
        @Query("q") language: String = "language:Kotlin",
        @Query("sort") sort: String = "stars",
        @Query("page") page: Int,
    ): RemoteGitHubRepositories?
}