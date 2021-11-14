package com.caioluis.data.mappers

import com.caioluis.data.local.model.LocalGitHubRepository
import com.caioluis.data.local.model.LocalRepositoryOwner
import com.caioluis.data.remote.model.RemoteGitHubRepository
import com.caioluis.data.remote.model.RemoteRepositoryOwner
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.entity.DomainRepositoryOwner

/**
 * Created by Caio Luis (caio-luis) on 10/10/20
 */

//Remote
fun RemoteGitHubRepository.toDomain(page: Int) =
    DomainGitHubRepository(
        id = id ?: 0,
        name = name ?: "",
        fullName = fullName ?: "",
        owner = owner?.toDomain() ?: DomainRepositoryOwner(),
        description = description ?: "",
        pullsUrl = pullsUrl ?: "",
        stargazersCount = stargazersCount ?: 0,
        forksCount = forksCount ?: 0,
        page = page
    )

fun RemoteRepositoryOwner.toDomain() =
    DomainRepositoryOwner(
        id = id ?: 0,
        login = login ?: "",
        avatarUrl = avatarUrl ?: ""
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
        page = page
    )

fun DomainRepositoryOwner.toLocal() =
    LocalRepositoryOwner(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )