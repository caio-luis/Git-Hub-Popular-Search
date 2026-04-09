package com.caioluis.githubpopular.data.impl.remote.githubpulls.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.PullRequestRemoteKey
import com.caioluis.githubpopular.data.impl.local.githubpulls.GithubPullRequestsLocalSource
import com.caioluis.githubpopular.data.impl.mapper.LocalGitHubPullRequestMapper
import com.caioluis.githubpopular.data.impl.mapper.RemotePullRequestMapper
import com.caioluis.githubpopular.data.impl.remote.githubpulls.source.PullRequestsRemoteSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class GitHubPullRequestsRemoteMediator @AssistedInject constructor(
    @Assisted private val pullUrl: String,
    @Assisted private val repositoryId: Int,
    private val remoteSource: PullRequestsRemoteSource,
    private val localSource: GithubPullRequestsLocalSource,
    private val errorMapper: ErrorMapper,
    private val remotePullRequestMapper: RemotePullRequestMapper,
    private val localGitHubPullRequestMapper: LocalGitHubPullRequestMapper,
) : RemoteMediator<Int, LocalGitHubPullRequest>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalGitHubPullRequest>,
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> STARTING_PAGE_INDEX

            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val remoteKey = localSource.getRemoteKey(repositoryId)

                remoteKey?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        Timber.d("Mediator load: loadType=%s, page=%d, repositoryId=%d", loadType, page, repositoryId)

        return try {
            val remotePullRequests = remoteSource.fetchPullRequests(pullUrl, page)
            val endOfPaginationReached = remotePullRequests.isEmpty()

            Timber.d(
                "Fetched %d pull requests from network: endOfPagination=%b, repositoryId=%d",
                remotePullRequests.size,
                endOfPaginationReached,
                repositoryId,
            )

            localSource.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localSource.deleteRemoteKey(repositoryId)
                    localSource.deletePullRequestsByRepositoryId(repositoryId)
                }

                localSource.insertRemoteKey(
                    PullRequestRemoteKey(
                        repositoryId = repositoryId,
                        nextPage = if (endOfPaginationReached) null else page + 1,
                    ),
                )

                localSource.savePullRequests(
                    remotePullRequests.mapIndexed { index, remotePullRequest ->
                        val domainPullRequest = remotePullRequestMapper.mapToDomain(remotePullRequest)
                        localGitHubPullRequestMapper.mapToLocal(
                            domainPullRequest = domainPullRequest,
                            repositoryId = repositoryId,
                            page = page,
                            orderInPage = index,
                        )
                    },
                )
                Timber.d(
                    "Persisted %d pull requests to Room: repositoryId=%d",
                    remotePullRequests.size,
                    repositoryId,
                )
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            handleLoadException(loadType, exception)
        }
    }

    private suspend fun handleLoadException(
        loadType: LoadType,
        exception: Exception,
    ): MediatorResult {
        Timber.w(exception, "Mediator load failed: loadType=%s, repositoryId=%d", loadType, repositoryId)

        if (loadType == LoadType.REFRESH) {
            val cachedItems = localSource.countPullRequestsByRepositoryId(repositoryId)

            if (cachedItems > 0) {
                Timber.i("Falling back to %d cached pull requests: repositoryId=%d", cachedItems, repositoryId)
                return MediatorResult.Success(endOfPaginationReached = false)
            }
        }

        return MediatorResult.Error(errorMapper.map(exception))
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
