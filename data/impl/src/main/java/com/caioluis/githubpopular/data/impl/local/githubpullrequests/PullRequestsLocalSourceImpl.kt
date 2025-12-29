package com.caioluis.githubpopular.data.impl.local.githubpullrequests

import com.caioluis.githubpopular.data.bridge.mappers.toDomain
import com.caioluis.githubpopular.data.bridge.mappers.toLocal
import com.caioluis.githubpopular.data.impl.local.githubpullrequests.dao.GitHubPullRequestsDao
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import javax.inject.Inject

class PullRequestsLocalSourceImpl
@Inject
constructor(
    private val gitHubPullRequestsDao: GitHubPullRequestsDao,
) : PullRequestsLocalSource {
    override suspend fun saveToLocalCache(
        pullRequests: List<DomainGitHubPullRequest>,
        repositoryId: Int,
    ) {
        gitHubPullRequestsDao.deletePullRequestsByRepositoryId(repositoryId)
        gitHubPullRequestsDao.savePullRequests(pullRequests.map { it.toLocal(repositoryId) })
    }

    override suspend fun getFromCache(
        repositoryId: Int,
    ): List<DomainGitHubPullRequest> = gitHubPullRequestsDao.getPullRequests(repositoryId).map {
        it.toDomain()
    }
}
