package com.caioluis.githubpopular.data.pub.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.caioluis.githubpopular.data.pub.local.LocalConstants.GIT_HUB_REPOSITORIES_TABLE_NAME

@Entity(tableName = GIT_HUB_REPOSITORIES_TABLE_NAME)
data class LocalGitHubRepository(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val fullName: String,
    val owner: LocalRepositoryOwner,
    val description: String,
    val pullsUrl: String,
    val stargazersCount: Int,
    val forksCount: Int,
    val htmlUrl: String,
    val page: Int,
    val language: String
)
