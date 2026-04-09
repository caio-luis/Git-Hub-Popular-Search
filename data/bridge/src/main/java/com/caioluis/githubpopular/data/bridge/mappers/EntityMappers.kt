package com.caioluis.githubpopular.data.bridge.mappers

import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.githubpulls.model.RemotePullRequest
import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteGitHubRepository
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository

interface RemoteGitHubRepositoryMapper {
    fun mapToDomain(
        remoteRepository: RemoteGitHubRepository,
        page: Int,
        language: String,
    ): DomainGitHubRepository
}

interface RemotePullRequestMapper {
    fun mapToDomain(remotePullRequest: RemotePullRequest): DomainGitHubPullRequest
}

interface LocalGitHubRepositoryMapper {
    fun mapToDomain(localRepository: LocalGitHubRepository): DomainGitHubRepository

    fun mapToLocal(domainRepository: DomainGitHubRepository): LocalGitHubRepository
}

interface LocalGitHubPullRequestMapper {
    fun mapToDomain(localPullRequest: LocalGitHubPullRequest): DomainGitHubPullRequest

    fun mapToLocal(
        domainPullRequest: DomainGitHubPullRequest,
        repositoryId: Int,
        page: Int = 1,
        orderInPage: Int = 0,
    ): LocalGitHubPullRequest
}
