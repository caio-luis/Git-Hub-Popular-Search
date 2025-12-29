package com.caioluis.githubpopular.data.bridge.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.caioluis.githubpopular.data.bridge.local.LocalConstants.GIT_HUB_PULL_REQUESTS_TABLE_NAME

@Entity(tableName = GIT_HUB_PULL_REQUESTS_TABLE_NAME)
data class LocalGitHubPullRequest(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val htmlUrl: String,
    val title: String,
    val body: String,
    val userName: String,
    val avatarUrl: String,
    val repositoryId: Int,
)
