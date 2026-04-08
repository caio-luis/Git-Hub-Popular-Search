package com.caioluis.githubpopular.data.bridge.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    val queryLanguage: String,
    val nextPage: Int?,
)
