package com.caioluis.githubpopular.data.impl.local.githubrepos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caioluis.githubpopular.data.bridge.local.LocalConstants
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.GitHubReposRemoteKey

@Dao
interface GitHubReposRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertOrReplace(remoteKey: GitHubReposRemoteKey)

    @Query("SELECT * FROM ${LocalConstants.REMOTE_KEYS_TABLE_NAME} WHERE queryLanguage = :language")
    suspend fun remoteKeyByQuery(language: String): GitHubReposRemoteKey?

    @Query("DELETE FROM ${LocalConstants.REMOTE_KEYS_TABLE_NAME} WHERE queryLanguage = :language")
    suspend fun deleteByQuery(language: String)
}
