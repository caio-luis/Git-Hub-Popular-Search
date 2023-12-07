package com.caioluis.githubpopular.data.pub.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caioluis.githubpopular.data.pub.local.model.LocalGitHubRepository

@Dao
interface GitHubRepositoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRepositories(gitHubRepositories: List<LocalGitHubRepository>)

    @Query(
        "SELECT * FROM GitHubRepositories WHERE page=:page AND language=:language " +
                "COLLATE NOCASE ORDER BY stargazersCount DESC"
    )
    suspend fun getAllRepositories(page: Int, language: String): List<LocalGitHubRepository>?

    @Query("DELETE FROM GitHubRepositories WHERE language=:language COLLATE NOCASE")
    suspend fun deleteReposByLanguage(language: String)
}
