package com.caioluis.githubpopular.mapper

import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.entity.DomainRepositoryOwner
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.model.UiRepositoryOwner

fun DomainGitHubRepository.toUi() =
    UiGitHubRepository(
        id = id,
        name = name,
        fullName = fullName,
        owner = owner.toUi(),
        description = description,
        pullsUrl = pullsUrl,
        stargazersCount = stargazersCount,
        forksCount = forksCount
    )

fun DomainRepositoryOwner.toUi() =
    UiRepositoryOwner(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )
