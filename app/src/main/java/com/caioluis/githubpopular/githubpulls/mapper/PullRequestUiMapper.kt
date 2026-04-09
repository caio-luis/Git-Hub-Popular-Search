package com.caioluis.githubpopular.githubpulls.mapper

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.githubpulls.model.UiGitHubPullRequest

interface PullRequestUiMapper {
    fun mapToUi(domainPullRequest: DomainGitHubPullRequest): UiGitHubPullRequest
}
