package com.caioluis.githubpopular.mapper

import com.caioluis.data.local.model.LocalRepositoryOwner
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.entity.DomainRepositoryOwner
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.model.UiRepositoryOwner

/**
 * Created by Caio Luis (caio-luis) on 12/10/20
 */

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