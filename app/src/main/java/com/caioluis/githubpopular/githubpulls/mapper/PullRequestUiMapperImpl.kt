package com.caioluis.githubpopular.githubpulls.mapper

import com.caioluis.githubpopular.core.common.extensions.truncate
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.githubpulls.model.UiGitHubPullRequest
import javax.inject.Inject

class PullRequestUiMapperImpl @Inject constructor() : PullRequestUiMapper {
    override fun mapToUi(domainPullRequest: DomainGitHubPullRequest) = UiGitHubPullRequest(
        id = domainPullRequest.id,
        title = domainPullRequest.title,
        body = domainPullRequest.body.truncate(BODY_CHAR_LIMIT),
        htmlUrl = domainPullRequest.htmlUrl,
        userName = domainPullRequest.userName,
        avatarUrl = domainPullRequest.avatarUrl,
    )

    companion object {
        private const val BODY_CHAR_LIMIT = 200
    }
}
