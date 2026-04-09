package com.caioluis.githubpopular.data.impl.remote.githubpulls

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.PullRequestRemoteKey
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.mapper.LocalGitHubPullRequestMapper
import com.caioluis.githubpopular.data.impl.mapper.RemotePullRequestMapper
import com.caioluis.githubpopular.data.impl.remote.githubpullrequests.PullRequestsRemoteSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GitHubPullRequestsRemoteMediator @AssistedInject constructor(
    @Assisted private val pullUrl: String,
    @Assisted private val repositoryId: Int,
    private val remoteSource: PullRequestsRemoteSource,
    private val localDatabase: GitHubReposDataBase,
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
                val remoteKey = localDatabase.withTransaction {
                    localDatabase.pullRequestRemoteKeysDao().remoteKeyByRepositoryId(repositoryId)
                }

                remoteKey?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return try {
            val remotePullRequests = remoteSource.fetchPullRequests(pullUrl, page)
            val endOfPaginationReached = remotePullRequests.isEmpty()

            localDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localDatabase.pullRequestRemoteKeysDao().deleteByRepositoryId(repositoryId)
                    localDatabase.gitHubPullRequestsDao()
                        .deletePullRequestsByRepositoryId(repositoryId)
                }

                localDatabase.pullRequestRemoteKeysDao().insertOrReplace(
                    PullRequestRemoteKey(
                        repositoryId = repositoryId,
                        nextPage = if (endOfPaginationReached) null else page + 1,
                    ),
                )

                localDatabase.gitHubPullRequestsDao().savePullRequests(
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
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            handleLoadException(loadType, exception)
        } catch (exception: HttpException) {
            handleLoadException(loadType, exception)
        } catch (exception: Exception) {
            handleLoadException(loadType, exception)
        }
    }

    private suspend fun handleLoadException(
        loadType: LoadType,
        exception: Exception,
    ): MediatorResult {
        if (loadType == LoadType.REFRESH) {
            val cachedItems = localDatabase.withTransaction {
                localDatabase.gitHubPullRequestsDao().countPullRequestsByRepositoryId(repositoryId)
            }

            if (cachedItems > 0) {
                return MediatorResult.Success(endOfPaginationReached = false)
            }
        }

        return MediatorResult.Error(errorMapper.map(exception))
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
