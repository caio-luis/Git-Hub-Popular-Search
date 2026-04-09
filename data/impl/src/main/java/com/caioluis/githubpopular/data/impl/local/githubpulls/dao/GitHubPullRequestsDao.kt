package com.caioluis.githubpopular.data.impl.local.githubpulls.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caioluis.githubpopular.data.bridge.local.LocalConstants.GIT_HUB_PULL_REQUESTS_TABLE_NAME
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest

@Dao
interface GitHubPullRequestsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePullRequests(pullRequests: List<LocalGitHubPullRequest>)

    @Query("SELECT * FROM $GIT_HUB_PULL_REQUESTS_TABLE_NAME WHERE repositoryId = :repositoryId ORDER BY page ASC, orderInPage ASC")
    fun getPagedPullRequests(repositoryId: Int): PagingSource<Int, LocalGitHubPullRequest>

    @Query("SELECT COUNT(*) FROM $GIT_HUB_PULL_REQUESTS_TABLE_NAME WHERE repositoryId = :repositoryId")
    suspend fun countPullRequestsByRepositoryId(repositoryId: Int): Int

    @Query("DELETE FROM $GIT_HUB_PULL_REQUESTS_TABLE_NAME WHERE repositoryId = :repositoryId")
    suspend fun deletePullRequestsByRepositoryId(repositoryId: Int)
}
