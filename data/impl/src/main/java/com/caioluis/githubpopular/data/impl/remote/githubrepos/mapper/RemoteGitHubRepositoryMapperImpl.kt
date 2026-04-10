package com.caioluis.githubpopular.data.impl.remote.githubrepos.mapper

import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteGitHubRepository
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import javax.inject.Inject

class RemoteGitHubRepositoryMapperImpl @Inject constructor() : RemoteGitHubRepositoryMapper {
    override fun mapToDomain(
        remoteRepository: RemoteGitHubRepository,
        page: Int,
        language: String,
    ) = DomainGitHubRepository(
        id = remoteRepository.id ?: -1,
        title = remoteRepository.name.orEmpty(),
        description = remoteRepository.description.orEmpty(),
        pullsUrl = remoteRepository.pullsUrl.orEmpty(),
        stargazersCount = remoteRepository.stargazersCount ?: 0,
        forksCount = remoteRepository.forksCount ?: 0,
        htmlUrl = remoteRepository.htmlUrl.orEmpty(),
        page = page,
        language = language,
        userName = remoteRepository.owner?.login.orEmpty(),
        avatarUrl = remoteRepository.owner?.avatarUrl.orEmpty(),
    )
}
