package com.caioluis.data.local.model

/**
 * Created by Caio Luis (@caio.luis) on 10/10/20
 */

data class LocalGitHubRepository(
    val id: Int,
    val name: String,
    val fullName: String,
    val owner: LocalRepositoryOwner,
    val description: String,
    val pullsUrl: String,
    val stargazersCount: Int,
    val forksCount: Int
)