package com.caioluis.githubpopular.data.impl.mapper

import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import javax.inject.Inject

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
