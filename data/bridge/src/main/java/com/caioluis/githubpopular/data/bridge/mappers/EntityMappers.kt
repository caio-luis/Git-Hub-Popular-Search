package com.caioluis.githubpopular.data.bridge.mappers

import com.caioluis.githubpopular.data.bridge.local.model.LocalGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.model.RemoteGitHubRepository
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository

// Remote
fun RemoteGitHubRepository.toDomain(
    page: Int,
    language: String,
) = DomainGitHubRepository(
    id = id ?: -1,
    title = name.orEmpty(),
    description = description.orEmpty(),
    pullsUrl = pullsUrl.orEmpty(),
    stargazersCount = stargazersCount ?: 0,
    forksCount = forksCount ?: 0,
    htmlUrl = htmlUrl.orEmpty(),
    page = page,
    language = language,
    userName = owner?.login.orEmpty(),
    avatarUrl = owner?.avatarUrl.orEmpty(),
)

// Local

fun LocalGitHubRepository.toDomain() = DomainGitHubRepository(
    id = id,
    title = title,
    description = description,
    pullsUrl = pullsUrl,
    stargazersCount = stargazersCount,
    forksCount = forksCount,
    htmlUrl = repositoryUrl,
    page = page,
    language = language,
    userName = userName,
    avatarUrl = avatarUrl,
)

// Domain

fun DomainGitHubRepository.toLocal() = LocalGitHubRepository(
    id = id,
    title = title,
    description = description,
    pullsUrl = pullsUrl,
    stargazersCount = stargazersCount,
    forksCount = forksCount,
    repositoryUrl = htmlUrl,
    page = page,
    language = language,
    userName = userName,
    avatarUrl = avatarUrl,
)
