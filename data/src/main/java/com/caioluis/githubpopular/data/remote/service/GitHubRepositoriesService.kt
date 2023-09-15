package com.caioluis.githubpopular.data.remote.service

import com.caioluis.githubpopular.data.remote.model.RemoteGitHubRepositories
import retrofit2.http.GET
import retrofit2.http.Query

const val DEFAULT_LANGUAGE_QUERY_BEGIN = "language:"
const val DEFAULT_LANGUAGE = "Kotlin"

interface GitHubRepositoriesService {

    @GET("search/repositories")
    suspend fun getGitHubRepositories(
        @Query("q") language: String = DEFAULT_LANGUAGE_QUERY_BEGIN + DEFAULT_LANGUAGE,
        @Query("sort") sort: String = "stars",
        @Query("page") page: Int,
    ): RemoteGitHubRepositories?
}
