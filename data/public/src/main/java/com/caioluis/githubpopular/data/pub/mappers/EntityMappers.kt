package com.caioluis.githubpopular.data.pub.mappers

import com.caioluis.githubpopular.data.pub.local.model.LocalGitHubRepository
import com.caioluis.githubpopular.data.pub.local.model.LocalRepositoryOwner
import com.caioluis.githubpopular.data.pub.remote.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.pub.remote.model.RemoteRepositoryOwner
import com.caioluis.githubpopular.domain.pub.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.pub.entity.DomainRepositoryOwner

//Remote
fun RemoteGitHubRepository.toDomain(page: Int, language: String) =
    DomainGitHubRepository(
        id = id ?: -1,
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
        language = language
    )

fun RemoteRepositoryOwner.toDomain() =
    DomainRepositoryOwner(
        id = id ?: -1,
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
        language = language,
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
        language = language,
    )

fun DomainRepositoryOwner.toLocal() =
    LocalRepositoryOwner(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
    )
