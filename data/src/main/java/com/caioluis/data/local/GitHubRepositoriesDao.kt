package com.caioluis.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caioluis.data.local.model.LocalGitHubRepository

/**
 * Created by Caio Luis (@caio.luis) on 02/11/20
 */

@Dao
interface GitHubRepositoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRepositories(gitHubRepositories: List<LocalGitHubRepository>)

    @Query("SELECT * FROM GitHubRepositories")
    suspend fun getAllRepositories(): List<LocalGitHubRepository>

    @Query("DELETE FROM GitHubRepositories")
    suspend fun deleteAllGitHubRepositories()
}