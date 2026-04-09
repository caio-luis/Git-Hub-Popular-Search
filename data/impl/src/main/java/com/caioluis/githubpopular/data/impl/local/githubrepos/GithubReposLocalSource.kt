package com.caioluis.githubpopular.data.impl.local.githubrepos

import androidx.paging.PagingSource
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.GitHubReposRemoteKey
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository

interface GithubReposLocalSource {
    suspend fun getRemoteKey(language: String): GitHubReposRemoteKey?

    suspend fun deleteRemoteKey(language: String)

    suspend fun insertRemoteKey(remoteKey: GitHubReposRemoteKey)

    suspend fun saveRepositories(repositories: List<LocalGitHubRepository>)

    suspend fun clearRepositories(language: String)

    fun getPagedRepositories(language: String): PagingSource<Int, LocalGitHubRepository>

    suspend fun <R> withTransaction(block: suspend () -> R): R
}
