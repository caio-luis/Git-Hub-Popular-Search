package com.caioluis.githubpopular.data.impl.local.githubrepos.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository

@Dao
interface GitHubRepositoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRepositories(gitHubRepositories: List<LocalGitHubRepository>)

    @Query("SELECT * FROM GitHubRepositories WHERE language = :language ORDER BY stargazersCount DESC")
    fun getPagedRepositories(language: String): PagingSource<Int, LocalGitHubRepository>

    @Query("DELETE FROM GitHubRepositories WHERE language = :language")
    suspend fun clearRepositories(language: String)
}
