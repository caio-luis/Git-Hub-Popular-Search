package com.caioluis.githubpopular.data.bridge.local.githubpulls.entity

import androidx.room.Entity
import androidx.room.Index
import com.caioluis.githubpopular.data.bridge.local.LocalConstants

@Entity(
    tableName = LocalConstants.GIT_HUB_PULL_REQUESTS_TABLE_NAME,
    primaryKeys = ["repositoryId", "id"],
    indices = [Index(value = ["repositoryId", "page", "orderInPage"])],
)
data class LocalGitHubPullRequest(
    val id: Long,
    val htmlUrl: String,
    val title: String,
    val body: String,
    val userName: String,
    val avatarUrl: String,
    val repositoryId: Int,
    val page: Int,
    val orderInPage: Int,
)
