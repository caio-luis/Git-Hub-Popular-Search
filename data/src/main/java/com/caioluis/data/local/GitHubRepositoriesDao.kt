package com.caioluis.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caioluis.data.local.model.LocalGitHubRepository

@Dao
interface GitHubRepositoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRepositories(gitHubRepositories: List<LocalGitHubRepository>)

    @Query("SELECT * FROM GitHubRepositories where page=:page order by stargazersCount desc")
    suspend fun getAllRepositories(page: Int): List<LocalGitHubRepository>?

    @Query("DELETE FROM GitHubRepositories")
    suspend fun deleteAllGitHubRepositories()
}
