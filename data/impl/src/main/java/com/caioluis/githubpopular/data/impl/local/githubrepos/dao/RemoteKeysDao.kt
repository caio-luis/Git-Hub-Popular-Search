package com.caioluis.githubpopular.data.impl.local.githubrepos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caioluis.githubpopular.data.bridge.local.model.RemoteKey

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKey)

    @Query("SELECT * FROM remote_keys WHERE queryLanguage = :language")
    suspend fun remoteKeyByQuery(language: String): RemoteKey?

    @Query("DELETE FROM remote_keys WHERE queryLanguage = :language")
    suspend fun deleteByQuery(language: String)
}
