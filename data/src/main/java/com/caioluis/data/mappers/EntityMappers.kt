package com.caioluis.data.mappers

import com.caioluis.data.local.model.LocalGitHubRepository
import com.caioluis.data.local.model.LocalRepositoryOwner
import com.caioluis.data.remote.model.RemoteGitHubRepository
import com.caioluis.data.remote.model.RemoteRepositoryOwner
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.entity.DomainRepositoryOwner

//Remote
fun RemoteGitHubRepository.toDomain(page: Int) =
    DomainGitHubRepository(
        id = id ?: 0,
        name = name ?: "",
        fullName = fullName ?: "",
        owner = owner?.toDomain()
            ?: DomainRepositoryOwner(),
        description = description ?: "",
        pullsUrl = pullsUrl ?: "",
        stargazersCount = stargazersCount ?: 0,
        forksCount = forksCount ?: 0,
        htmlUrl = htmlUrl ?: "",
        page = page,
    )

fun RemoteRepositoryOwner.toDomain() =
    DomainRepositoryOwner(
        id = id ?: 0,
        login = login ?: "",
        avatarUrl = avatarUrl ?: "",
    )

// Local

fun LocalGitHubRepository.toDomain() =
    DomainGitHubRepository(
        id = id,
        name = name,
        fullName = fullName,
        owner = owner.toDomain(),
        description = description,
        pullsUrl = pullsUrl,
        stargazersCount = stargazersCount,
        forksCount = forksCount,
        htmlUrl = htmlUrl,
        page = page,
    )

fun LocalRepositoryOwner.toDomain() =
    DomainRepositoryOwner(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
    )

// Domain

fun DomainGitHubRepository.toLocal() =
    LocalGitHubRepository(
        id = id,
        name = name,
        fullName = fullName,
        owner = owner.toLocal(),
        description = description,
        pullsUrl = pullsUrl,
        stargazersCount = stargazersCount,
        forksCount = forksCount,
        htmlUrl = htmlUrl,
        page = page,
    )

fun DomainRepositoryOwner.toLocal() =
    LocalRepositoryOwner(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
    )
