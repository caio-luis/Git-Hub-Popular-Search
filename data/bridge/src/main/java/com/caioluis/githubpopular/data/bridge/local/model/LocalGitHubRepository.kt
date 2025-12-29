package com.caioluis.githubpopular.data.bridge.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.caioluis.githubpopular.data.bridge.local.LocalConstants.GIT_HUB_REPOSITORIES_TABLE_NAME

@Entity(tableName = GIT_HUB_REPOSITORIES_TABLE_NAME)
data class LocalGitHubRepository(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val description: String,
    val pullsUrl: String,
    val stargazersCount: Int,
    val forksCount: Int,
    val repositoryUrl: String,
    val page: Int,
    val language: String,
    val userName: String,
    val avatarUrl: String,
)
