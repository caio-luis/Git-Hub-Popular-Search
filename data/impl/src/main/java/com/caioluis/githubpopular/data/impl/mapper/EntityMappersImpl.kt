package com.caioluis.githubpopular.data.impl.mapper

import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.data.bridge.mappers.LocalGitHubPullRequestMapper
import com.caioluis.githubpopular.data.bridge.mappers.LocalGitHubRepositoryMapper
import com.caioluis.githubpopular.data.bridge.mappers.RemoteGitHubRepositoryMapper
import com.caioluis.githubpopular.data.bridge.mappers.RemotePullRequestMapper
import com.caioluis.githubpopular.data.bridge.remote.githubpulls.model.RemotePullRequest
import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteGitHubRepository
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
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

class RemotePullRequestMapperImpl @Inject constructor() : RemotePullRequestMapper {
    override fun mapToDomain(remotePullRequest: RemotePullRequest) = DomainGitHubPullRequest(
        id = remotePullRequest.id ?: -1L,
        htmlUrl = remotePullRequest.url.orEmpty(),
        title = remotePullRequest.title.orEmpty(),
        body = remotePullRequest.body.orEmpty(),
        userName = remotePullRequest.user?.login.orEmpty(),
        avatarUrl = remotePullRequest.user?.avatarUrl.orEmpty(),
    )
}

class LocalGitHubRepositoryMapperImpl @Inject constructor() : LocalGitHubRepositoryMapper {
    override fun mapToDomain(localRepository: LocalGitHubRepository) = DomainGitHubRepository(
        id = localRepository.id,
        title = localRepository.title,
        description = localRepository.description,
        pullsUrl = localRepository.pullsUrl,
        stargazersCount = localRepository.stargazersCount,
        forksCount = localRepository.forksCount,
        htmlUrl = localRepository.repositoryUrl,
        page = localRepository.page,
        language = localRepository.language,
        userName = localRepository.userName,
        avatarUrl = localRepository.avatarUrl,
    )

    override fun mapToLocal(domainRepository: DomainGitHubRepository) = LocalGitHubRepository(
        id = domainRepository.id,
        title = domainRepository.title,
        description = domainRepository.description,
        pullsUrl = domainRepository.pullsUrl,
        stargazersCount = domainRepository.stargazersCount,
        forksCount = domainRepository.forksCount,
        repositoryUrl = domainRepository.htmlUrl,
        page = domainRepository.page,
        language = domainRepository.language,
        userName = domainRepository.userName,
        avatarUrl = domainRepository.avatarUrl,
    )
}

class LocalGitHubPullRequestMapperImpl @Inject constructor() : LocalGitHubPullRequestMapper {
    override fun mapToDomain(localPullRequest: LocalGitHubPullRequest) = DomainGitHubPullRequest(
        id = localPullRequest.id,
        htmlUrl = localPullRequest.htmlUrl,
        title = localPullRequest.title,
        body = localPullRequest.body,
        userName = localPullRequest.userName,
        avatarUrl = localPullRequest.avatarUrl,
    )

    override fun mapToLocal(
        domainPullRequest: DomainGitHubPullRequest,
        repositoryId: Int,
        page: Int,
        orderInPage: Int,
    ) = LocalGitHubPullRequest(
        id = domainPullRequest.id,
        htmlUrl = domainPullRequest.htmlUrl,
        title = domainPullRequest.title,
        body = domainPullRequest.body,
        userName = domainPullRequest.userName,
        avatarUrl = domainPullRequest.avatarUrl,
        repositoryId = repositoryId,
        page = page,
        orderInPage = orderInPage,
    )
}
