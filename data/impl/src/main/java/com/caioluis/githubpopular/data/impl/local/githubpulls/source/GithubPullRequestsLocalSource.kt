package com.caioluis.githubpopular.data.impl.local.githubpulls.source

import androidx.paging.PagingSource
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.PullRequestRemoteKey

interface GithubPullRequestsLocalSource {
    suspend fun getRemoteKey(repositoryId: Int): PullRequestRemoteKey?

    suspend fun deleteRemoteKey(repositoryId: Int)

    suspend fun insertRemoteKey(remoteKey: PullRequestRemoteKey)

    suspend fun savePullRequests(pullRequests: List<LocalGitHubPullRequest>)

    suspend fun deletePullRequestsByRepositoryId(repositoryId: Int)

    suspend fun countPullRequestsByRepositoryId(repositoryId: Int): Int

    fun getPagedPullRequests(repositoryId: Int): PagingSource<Int, LocalGitHubPullRequest>

    suspend fun <R> withTransaction(block: suspend () -> R): R
}
