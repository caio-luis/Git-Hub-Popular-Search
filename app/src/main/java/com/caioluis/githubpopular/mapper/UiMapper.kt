package com.caioluis.githubpopular.mapper

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.entity.DomainRepositoryOwner
import com.caioluis.githubpopular.extensions.truncate
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.model.UiRepositoryOwner

private const val DESCRIPTION_CHAR_LIMIT = 300
fun DomainGitHubRepository.toUi() = UiGitHubRepository(
    id = id,
    name = name,
    fullName = fullName,
    owner = owner.toUi(),
    description = description.truncate(DESCRIPTION_CHAR_LIMIT),
    pullsUrl = pullsUrl,
    stargazersCount = stargazersCount,
    htmlUrl = htmlUrl,
    forksCount = forksCount,
)

fun DomainRepositoryOwner.toUi() = UiRepositoryOwner(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
)
