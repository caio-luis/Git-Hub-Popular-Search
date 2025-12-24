package com.caioluis.githubpopular.data.impl.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.caioluis.githubpopular.data.bridge.local.dao.GitHubRepositoriesDao
import com.caioluis.githubpopular.data.bridge.local.model.LocalGitHubRepository
import com.caioluis.githubpopular.data.impl.local.typeconverter.RepositoryOwnerConverter

const val DATABASE_FILE_NAME = "GitHubPopular.db"

@Database(
    entities = [LocalGitHubRepository::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(RepositoryOwnerConverter::class)
abstract class GitHubReposDataBase : RoomDatabase() {
    abstract fun gitHubRepositoriesDao(): GitHubRepositoriesDao

    companion object {
        private var dbInstance: GitHubReposDataBase? = null

        fun getInstance(context: Context): GitHubReposDataBase = dbInstance ?: synchronized(this) {
            dbInstance ?: buildDatabase(context).also { dbInstance = it }
        }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(
                context.applicationContext,
                GitHubReposDataBase::class.java,
                DATABASE_FILE_NAME,
            ).fallbackToDestructiveMigration(dropAllTables = true)
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .build()
    }
}
