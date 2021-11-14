package com.caioluis.domain.entity

/**
 * Created by Caio Luis (caio-luis) on 10/10/20
 */
data class DomainGitHubRepository(
    val id: Int = 0,
    val name: String = "",
    val fullName: String = "",
    val owner: DomainRepositoryOwner = DomainRepositoryOwner(),
    val description: String = "",
    val pullsUrl: String = "",
    val stargazersCount: Int = 0,
    val forksCount: Int = 0,
    val page: Int = 0
)