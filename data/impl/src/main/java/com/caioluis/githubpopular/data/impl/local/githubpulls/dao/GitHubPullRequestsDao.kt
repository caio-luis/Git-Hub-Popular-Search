package com.caioluis.githubpopular.data.impl.local.githubpulls.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caioluis.githubpopular.data.bridge.local.LocalConstants.GIT_HUB_PULL_REQUESTS_TABLE_NAME
import com.caioluis.githubpopular.data.bridge.local.model.LocalGitHubPullRequest

@Dao
interface GitHubPullRequestsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePullRequests(pullRequests: List<LocalGitHubPullRequest>)

    @Query("SELECT * FROM $GIT_HUB_PULL_REQUESTS_TABLE_NAME WHERE repositoryId = :repositoryId")
    suspend fun getPullRequests(repositoryId: Int): List<LocalGitHubPullRequest>

    @Query("DELETE FROM $GIT_HUB_PULL_REQUESTS_TABLE_NAME WHERE repositoryId = :repositoryId")
    suspend fun deletePullRequestsByRepositoryId(repositoryId: Int)
}
