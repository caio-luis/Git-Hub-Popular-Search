package com.caioluis.githubpopular.data.impl.remote.githubrepos

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.GitHubReposRemoteKey
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.data.bridge.mappers.LocalGitHubRepositoryMapper
import com.caioluis.githubpopular.data.bridge.mappers.RemoteGitHubRepositoryMapper
import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GitHubReposRemoteMediator @AssistedInject constructor(
    @Assisted private val language: String,
    private val remoteSource: GithubReposRemoteSource,
    private val localDatabase: GitHubReposDataBase,
    private val errorMapper: ErrorMapper,
    private val remoteGitHubRepositoryMapper: RemoteGitHubRepositoryMapper,
    private val localGitHubRepositoryMapper: LocalGitHubRepositoryMapper,
) : RemoteMediator<Int, LocalGitHubRepository>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalGitHubRepository>,
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> STARTING_PAGE_INDEX

            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val remoteKey = localDatabase.withTransaction {
                    localDatabase.remoteKeysDao().remoteKeyByQuery(language)
                }

                if (remoteKey?.nextPage == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKey.nextPage
            }
        }

        return try {
            val remoteRepositories: List<RemoteGitHubRepository?> =
                page?.let { remoteSource.fetchFromRemote(it, language) } ?: emptyList()
            val endOfPaginationReached = remoteRepositories.isEmpty()

            localDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localDatabase.remoteKeysDao().deleteByQuery(language)
                    localDatabase.gitHubRepositoriesDao().clearRepositories(language)
                }

                val nextPage = if (endOfPaginationReached) null else page?.plus(1)
                localDatabase.remoteKeysDao().insertOrReplace(
                    GitHubReposRemoteKey(queryLanguage = language, nextPage = nextPage),
                )

                val localEntities: List<LocalGitHubRepository> =
                    remoteRepositories.mapNotNull { remoteRepository ->
                        page?.let { currentPage ->
                            remoteRepository?.let {
                                remoteGitHubRepositoryMapper.mapToDomain(
                                    remoteRepository = it,
                                    page = currentPage,
                                    language = language,
                                )
                            }
                        }
                    }.map(localGitHubRepositoryMapper::mapToLocal)

                localDatabase.gitHubRepositoriesDao().saveRepositories(localEntities)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            MediatorResult.Error(errorMapper.map(exception))
        } catch (exception: HttpException) {
            MediatorResult.Error(errorMapper.map(exception))
        } catch (exception: Exception) {
            MediatorResult.Error(errorMapper.map(exception))
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
