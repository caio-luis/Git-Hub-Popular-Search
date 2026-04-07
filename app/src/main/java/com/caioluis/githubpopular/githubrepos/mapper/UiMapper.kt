package com.caioluis.githubpopular.githubrepos.mapper

import com.caioluis.githubpopular.core.common.extensions.truncate
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.githubrepos.model.UiGitHubRepo

private const val DESCRIPTION_CHAR_LIMIT = 300
fun DomainGitHubRepository.toUi() = UiGitHubRepo(
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
