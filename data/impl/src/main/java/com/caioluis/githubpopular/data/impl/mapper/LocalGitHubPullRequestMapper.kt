package com.caioluis.githubpopular.data.impl.mapper

import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest

interface LocalGitHubPullRequestMapper {
    fun mapToDomain(localPullRequest: LocalGitHubPullRequest): DomainGitHubPullRequest

    fun mapToLocal(
        domainPullRequest: DomainGitHubPullRequest,
        repositoryId: Int,
        page: Int = 1,
        orderInPage: Int = 0,
    ): LocalGitHubPullRequest
}
