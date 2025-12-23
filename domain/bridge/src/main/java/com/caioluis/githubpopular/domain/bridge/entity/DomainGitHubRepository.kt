package com.caioluis.githubpopular.domain.bridge.entity

data class DomainGitHubRepository(
    val id: Int = 0,
    val name: String = "",
    val fullName: String = "",
    val owner: DomainRepositoryOwner = DomainRepositoryOwner(),
    val description: String = "",
    val pullsUrl: String = "",
    val stargazersCount: Int = 0,
    val forksCount: Int = 0,
    val htmlUrl: String = "",
    val page: Int = 0,
    val language: String = "",
)
