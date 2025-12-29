package com.caioluis.githubpopular.mapper

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.extensions.truncate
import com.caioluis.githubpopular.model.UiGitHubPullRequest

private const val BODY_CHAR_LIMIT = 200

fun DomainGitHubPullRequest.toUi() = UiGitHubPullRequest(
    id = id,
    title = title,
    body = body.truncate(BODY_CHAR_LIMIT),
    htmlUrl = htmlUrl,
    userName = userName,
    avatarUrl = avatarUrl,
)
