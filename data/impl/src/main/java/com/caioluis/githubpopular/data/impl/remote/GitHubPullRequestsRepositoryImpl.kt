package com.caioluis.githubpopular.data.impl.remote

import com.caioluis.githubpopular.core.common.utils.LogUtil
import com.caioluis.githubpopular.data.bridge.mappers.toDomain
import com.caioluis.githubpopular.data.impl.local.githubpullrequests.PullRequestsLocalSource
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.repository.GitHubPullRequestsRepository
import javax.inject.Inject

class GitHubPullRequestsRepositoryImpl
@Inject
constructor(
    private val remoteSource: PullRequestsRemoteSource,
    private val localSource: PullRequestsLocalSource,
) : GitHubPullRequestsRepository {
    override suspend fun getPullRequests(
        pullUrl: String,
        repositoryId: Int,
        page: Int,
    ): List<DomainGitHubPullRequest> = runCatching {
        remoteSource.fetchPullRequests(pullUrl, page)
            .takeIf { it.isNotEmpty() }
            ?.map { it.toDomain() }
            ?.also { items ->
                localSource.saveToLocalCache(items, repositoryId)
            } ?: throw NoMoreItemsException()
    }.getOrElse { previousError ->
        if (page == 1) {
            localSource.getFromCache(repositoryId)
                .takeIf { it.isNotEmpty() }
                ?: run {
                    LogUtil.e(
                        tag = GitHubPullRequestsRepositoryImpl::class.simpleName,
                        message = previousError.message,
                        throwable = previousError,
                    )
                    throw previousError
                }
        } else {
            LogUtil.e(
                tag = GitHubPullRequestsRepositoryImpl::class.simpleName,
                message = previousError.message,
                throwable = previousError,
            )
            throw previousError
        }
    }
}
