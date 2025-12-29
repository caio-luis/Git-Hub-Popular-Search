package com.caioluis.githubpopular.mapper

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.extensions.truncate
import com.caioluis.githubpopular.model.UiGitHubRepository

private const val DESCRIPTION_CHAR_LIMIT = 300
fun DomainGitHubRepository.toUi() = UiGitHubRepository(
    id = id,
    title = title,
    description = description.truncate(DESCRIPTION_CHAR_LIMIT),
    pullsUrl = pullsUrl.replace("{/number}", ""),
    stargazersCount = stargazersCount,
    repositoryUrl = htmlUrl,
    forksCount = forksCount,
    userName = userName,
    avatarUrl = avatarUrl,
)
