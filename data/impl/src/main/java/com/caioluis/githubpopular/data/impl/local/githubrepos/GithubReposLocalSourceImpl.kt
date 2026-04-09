package com.caioluis.githubpopular.data.impl.local.githubrepos

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.GitHubReposRemoteKey
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.local.GitHubReposRemoteKeysDao
import com.caioluis.githubpopular.data.impl.local.githubrepos.dao.GitHubRepositoriesDao
import timber.log.Timber
import javax.inject.Inject

class GithubReposLocalSourceImpl @Inject constructor(
    private val localDatabase: GitHubReposDataBase,
    private val repositoriesDao: GitHubRepositoriesDao,
    private val remoteKeysDao: GitHubReposRemoteKeysDao,
) : GithubReposLocalSource {

    override suspend fun getRemoteKey(language: String): GitHubReposRemoteKey? = remoteKeysDao.remoteKeyByQuery(language)

    override suspend fun deleteRemoteKey(language: String) = remoteKeysDao.deleteByQuery(language)

    override suspend fun insertRemoteKey(remoteKey: GitHubReposRemoteKey) = remoteKeysDao.insertOrReplace(remoteKey)

    override suspend fun saveRepositories(repositories: List<LocalGitHubRepository>) {
        Timber.d("Saving %d repositories to Room", repositories.size)
        repositoriesDao.saveRepositories(repositories)
    }

    override suspend fun clearRepositories(language: String) {
        Timber.d("Clearing repositories from Room: language=%s", language)
        repositoriesDao.clearRepositories(language)
    }

    override suspend fun countRepositoriesByLanguage(language: String): Int = repositoriesDao.countRepositoriesByLanguage(language)

    override fun getPagedRepositories(language: String): PagingSource<Int, LocalGitHubRepository> = repositoriesDao.getPagedRepositories(language)

    override suspend fun <R> withTransaction(block: suspend () -> R): R = localDatabase.withTransaction { block() }
}
