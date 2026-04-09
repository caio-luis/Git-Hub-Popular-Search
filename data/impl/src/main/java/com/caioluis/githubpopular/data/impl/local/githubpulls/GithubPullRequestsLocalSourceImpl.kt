package com.caioluis.githubpopular.data.impl.local.githubpulls

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.PullRequestRemoteKey
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.local.PullRequestRemoteKeysDao
import com.caioluis.githubpopular.data.impl.local.githubpulls.dao.GitHubPullRequestsDao
import javax.inject.Inject

class GithubPullRequestsLocalSourceImpl @Inject constructor(
    private val localDatabase: GitHubReposDataBase,
    private val pullRequestsDao: GitHubPullRequestsDao,
    private val pullRequestRemoteKeysDao: PullRequestRemoteKeysDao,
) : GithubPullRequestsLocalSource {

    override suspend fun getRemoteKey(repositoryId: Int): PullRequestRemoteKey? = pullRequestRemoteKeysDao.remoteKeyByRepositoryId(repositoryId)

    override suspend fun deleteRemoteKey(repositoryId: Int) = pullRequestRemoteKeysDao.deleteByRepositoryId(repositoryId)

    override suspend fun insertRemoteKey(remoteKey: PullRequestRemoteKey) = pullRequestRemoteKeysDao.insertOrReplace(remoteKey)

    override suspend fun savePullRequests(pullRequests: List<LocalGitHubPullRequest>) = pullRequestsDao.savePullRequests(pullRequests)

    override suspend fun deletePullRequestsByRepositoryId(repositoryId: Int) = pullRequestsDao.deletePullRequestsByRepositoryId(repositoryId)

    override suspend fun countPullRequestsByRepositoryId(repositoryId: Int): Int = pullRequestsDao.countPullRequestsByRepositoryId(repositoryId)

    override fun getPagedPullRequests(repositoryId: Int): PagingSource<Int, LocalGitHubPullRequest> = pullRequestsDao.getPagedPullRequests(repositoryId)

    override suspend fun <R> withTransaction(block: suspend () -> R): R = localDatabase.withTransaction { block() }
}
