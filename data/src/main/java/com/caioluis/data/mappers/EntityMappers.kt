package com.caioluis.data.mappers

import com.caioluis.data.local.model.LocalGitHubRepository
import com.caioluis.data.local.model.LocalRepositoryOwner
import com.caioluis.data.remote.model.RemoteGitHubRepository
import com.caioluis.data.remote.model.RemoteRepositoryOwner
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.entity.DomainRepositoryOwner

/**
 * Created by Caio Luis (@caio.luis) on 10/10/20
 */

//Remote
fun RemoteGitHubRepository.toDomain() =
    DomainGitHubRepository(
        id = id,
        name = name,
        fullName = fullName,
        owner = owner.toDomain(),
        description = description,
        pullsUrl = pullsUrl,
        stargazersCount = stargazersCount,
        forksCount = forksCount
    )

fun RemoteRepositoryOwner.toDomain() =
    DomainRepositoryOwner(
        id = id,
        login = login,
        avatarUrl = avatarUrl
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
        forksCount = forksCount
    )

fun LocalRepositoryOwner.toDomain() =
    DomainRepositoryOwner(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )

// Domain
fun DomainGitHubRepository.toRemote() =
    RemoteGitHubRepository(
        id = id,
        name = name,
        fullName = fullName,
        owner = owner.toRemote(),
        description = description,
        pullsUrl = pullsUrl,
        stargazersCount = stargazersCount,
        forksCount = forksCount
    )

fun DomainRepositoryOwner.toRemote() =
    RemoteRepositoryOwner(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )

fun DomainGitHubRepository.toLocal() =
    LocalGitHubRepository(
        id = id,
        name = name,
        fullName = fullName,
        owner = owner.toLocal(),
        description = description,
        pullsUrl = pullsUrl,
        stargazersCount = stargazersCount,
        forksCount = forksCount
    )

fun DomainRepositoryOwner.toLocal() =
    LocalRepositoryOwner(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )