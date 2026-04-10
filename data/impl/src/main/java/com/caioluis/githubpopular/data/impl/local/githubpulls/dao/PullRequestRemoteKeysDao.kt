package com.caioluis.githubpopular.data.impl.local.githubpulls.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caioluis.githubpopular.data.bridge.local.LocalConstants
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.PullRequestRemoteKey

@Dao
interface PullRequestRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertOrReplace(remoteKey: PullRequestRemoteKey)

    @Query("SELECT * FROM ${LocalConstants.PULL_REQUEST_REMOTE_KEYS_TABLE_NAME} WHERE repositoryId = :repositoryId")
    suspend fun remoteKeyByRepositoryId(repositoryId: Int): PullRequestRemoteKey?

    @Query("DELETE FROM ${LocalConstants.PULL_REQUEST_REMOTE_KEYS_TABLE_NAME} WHERE repositoryId = :repositoryId")
    suspend fun deleteByRepositoryId(repositoryId: Int)
}
