package com.caioluis.githubpopular.data.impl.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.caioluis.githubpopular.data.bridge.local.model.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.bridge.local.model.LocalGitHubRepository
import com.caioluis.githubpopular.data.bridge.local.model.RemoteKey
import com.caioluis.githubpopular.data.impl.local.githubpulls.dao.GitHubPullRequestsDao
import com.caioluis.githubpopular.data.impl.local.githubrepos.dao.GitHubRepositoriesDao
import com.caioluis.githubpopular.data.impl.local.githubrepos.dao.RemoteKeysDao

const val DATABASE_FILE_NAME = "GitHubPopular.db"

@Database(
    entities = [LocalGitHubRepository::class, LocalGitHubPullRequest::class, RemoteKey::class],
    version = 1,
    exportSchema = false,
)
abstract class GitHubReposDataBase : RoomDatabase() {
    abstract fun gitHubRepositoriesDao(): GitHubRepositoriesDao
    abstract fun gitHubPullRequestsDao(): GitHubPullRequestsDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        private var dbInstance: GitHubReposDataBase? = null

        fun getInstance(context: Context): GitHubReposDataBase = dbInstance ?: synchronized(this) {
            dbInstance ?: buildDatabase(context).also { dbInstance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            GitHubReposDataBase::class.java,
            DATABASE_FILE_NAME,
        ).fallbackToDestructiveMigration(dropAllTables = true)
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .build()
    }
}
