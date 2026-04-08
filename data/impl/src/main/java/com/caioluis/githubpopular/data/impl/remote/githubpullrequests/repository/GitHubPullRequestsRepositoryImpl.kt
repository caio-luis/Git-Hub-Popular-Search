package com.caioluis.githubpopular.data.impl.remote.githubpullrequests.repository

import com.caioluis.githubpopular.data.bridge.mappers.toDomain
import com.caioluis.githubpopular.data.impl.local.githubpulls.PullRequestsLocalSource
import com.caioluis.githubpopular.data.impl.remote.githubpullrequests.PullRequestsRemoteSource
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.repository.GitHubPullRequestsRepository
import timber.log.Timber
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
            } ?: emptyList()
    }.getOrElse { previousError ->
        if (page == 1) {
            localSource.getFromCache(repositoryId)
                .takeIf { it.isNotEmpty() }
                ?: run {
                    Timber.e(
                        previousError,
                        "Failed to load pull requests for repository $repositoryId",
                    )
                    throw previousError
                }
        } else {
            Timber.e(
                previousError,
                "Failed to load pull requests page $page for url $pullUrl",
            )
            throw previousError
        }
    }
}
