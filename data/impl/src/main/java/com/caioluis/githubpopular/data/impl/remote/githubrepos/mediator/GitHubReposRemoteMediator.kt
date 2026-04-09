package com.caioluis.githubpopular.data.impl.remote.githubrepos.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.GitHubReposRemoteKey
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.impl.local.githubrepos.GithubReposLocalSource
import com.caioluis.githubpopular.data.impl.mapper.LocalGitHubRepositoryMapper
import com.caioluis.githubpopular.data.impl.mapper.RemoteGitHubRepositoryMapper
import com.caioluis.githubpopular.data.impl.remote.githubrepos.source.GithubReposRemoteSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class GitHubReposRemoteMediator @AssistedInject constructor(
    @Assisted private val language: String,
    private val remoteSource: GithubReposRemoteSource,
    private val localSource: GithubReposLocalSource,
    private val errorMapper: ErrorMapper,
    private val remoteGitHubRepositoryMapper: RemoteGitHubRepositoryMapper,
    private val localGitHubRepositoryMapper: LocalGitHubRepositoryMapper,
) : RemoteMediator<Int, LocalGitHubRepository>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalGitHubRepository>,
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> STARTING_PAGE_INDEX

            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val remoteKey = localSource.getRemoteKey(language)
                remoteKey?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        Timber.d("Mediator load: loadType=%s, page=%d, language=%s", loadType, page, language)

        return try {
            val remoteRepositories: List<RemoteGitHubRepository> =
                remoteSource.fetchFromRemote(page, language)
            val endOfPaginationReached = remoteRepositories.isEmpty()

            Timber.d(
                "Fetched %d repositories from network: endOfPagination=%b, language=%s",
                remoteRepositories.size,
                endOfPaginationReached,
                language,
            )

            localSource.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localSource.deleteRemoteKey(language)
                    localSource.clearRepositories(language)
                }

                val nextPage = if (endOfPaginationReached) null else page + 1
                localSource.insertRemoteKey(
                    GitHubReposRemoteKey(queryLanguage = language, nextPage = nextPage),
                )

                val localEntities: List<LocalGitHubRepository> =
                    remoteRepositories
                        .map { remoteRepository ->
                            remoteGitHubRepositoryMapper.mapToDomain(
                                remoteRepository = remoteRepository,
                                page = page,
                                language = language,
                            )
                        }
                        .map(localGitHubRepositoryMapper::mapToLocal)

                localSource.saveRepositories(localEntities)
                Timber.d("Persisted %d repositories to Room: language=%s", localEntities.size, language)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (exception: Exception) {
            handleLoadException(loadType, exception)
        }
    }

    private suspend fun handleLoadException(
        loadType: LoadType,
        exception: Exception,
    ): MediatorResult {
        Timber.w(exception, "Mediator load failed: loadType=%s, language=%s", loadType, language)

        if (loadType == LoadType.REFRESH) {
            val cachedItems = localSource.countRepositoriesByLanguage(language)

            if (cachedItems > 0) {
                Timber.i("Falling back to %d cached repositories: language=%s", cachedItems, language)
                return MediatorResult.Success(endOfPaginationReached = false)
            }
        }

        return MediatorResult.Error(errorMapper.map(exception))
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
