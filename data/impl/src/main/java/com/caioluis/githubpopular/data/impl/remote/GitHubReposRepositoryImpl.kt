package com.caioluis.githubpopular.data.impl.remote

import com.caioluis.githubpopular.core.common.utils.LogUtil
import com.caioluis.githubpopular.data.bridge.mappers.toDomain
import com.caioluis.githubpopular.data.impl.local.githubrepos.LocalSource
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import javax.inject.Inject

class GitHubReposRepositoryImpl
@Inject
constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource,
) : GitHubReposRepository {
    override suspend fun getGitHubRepositories(
        page: Int,
        language: String,
    ): List<DomainGitHubRepository> = runCatching {
        remoteSource.fetchFromRemote(page, language)
            ?.mapNotNull { it?.toDomain(page, language) }
            ?.takeIf { it.isNotEmpty() }
            ?.also { items ->
                localSource.saveToLocalCache(items, page, language)
            } ?: emptyList()
    }.getOrElse { previousError ->
        localSource.getFromCache(page, language)
            .takeIf { it.isNotEmpty() }
            ?: run {
                LogUtil.e(
                    tag = GitHubReposRepositoryImpl::class.simpleName,
                    message = previousError.message,
                    throwable = previousError,
                )
                throw previousError
            }
    }
}
