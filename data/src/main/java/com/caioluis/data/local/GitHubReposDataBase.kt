package com.caioluis.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.caioluis.data.local.model.LocalGitHubRepository
import com.caioluis.data.local.type_converter.RepositoryOwnerConverter

/**
 * Created by Caio Luis (@caio.luis) on 12/10/20
 */

@Database(
    entities = [LocalGitHubRepository::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(RepositoryOwnerConverter::class)
abstract class GitHubReposDataBase : RoomDatabase() {

    abstract fun gitHubRepositoriesDao() : GitHubRepositoriesDao

    companion object {

        private var INSTANCE: GitHubReposDataBase? = null

        fun getInstance(context: Context): GitHubReposDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GitHubReposDataBase::class.java, "GitHubPopular.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}