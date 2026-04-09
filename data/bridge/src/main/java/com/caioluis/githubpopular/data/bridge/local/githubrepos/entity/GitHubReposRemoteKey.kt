package com.caioluis.githubpopular.data.bridge.local.githubrepos.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.caioluis.githubpopular.data.bridge.local.LocalConstants

@Entity(tableName = LocalConstants.REMOTE_KEYS_TABLE_NAME)
data class GitHubReposRemoteKey(
    @PrimaryKey
    val queryLanguage: String,
    val nextPage: Int?,
)
