package com.caioluis.githubpopular.data.impl.remote.githubrepos.mapper

import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteGitHubRepository
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository

interface RemoteGitHubRepositoryMapper {
    fun mapToDomain(
        remoteRepository: RemoteGitHubRepository,
        page: Int,
        language: String,
    ): DomainGitHubRepository
}
