package com.caioluis.githubpopular.data.bridge.local.githubpulls.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.caioluis.githubpopular.data.bridge.local.LocalConstants

@Entity(tableName = LocalConstants.PULL_REQUEST_REMOTE_KEYS_TABLE_NAME)
data class PullRequestRemoteKey(
    @PrimaryKey
    val repositoryId: Int,
    val nextPage: Int?,
)
