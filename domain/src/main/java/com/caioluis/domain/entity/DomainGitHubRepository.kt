package com.caioluis.domain.entity

/**
 * Created by Caio Luis (@caio.luis) on 10/10/20
 */
data class DomainGitHubRepository(
    val id: Int,
    val name: String,
    val fullName: String,
    val owner: DomainRepositoryOwner,
    val description: String,
    val pullsUrl: String,
    val stargazersCount: Int,
    val forksCount: Int
)