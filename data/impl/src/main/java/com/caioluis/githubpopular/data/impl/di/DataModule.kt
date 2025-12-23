package com.caioluis.githubpopular.data.impl.di

import android.content.Context
import com.caioluis.githubpopular.data.bridge.local.LocalSource
import com.caioluis.githubpopular.data.bridge.local.dao.GitHubRepositoriesDao
import com.caioluis.githubpopular.data.bridge.remote.RemoteSource
import com.caioluis.githubpopular.data.bridge.remote.service.GitHubRepositoriesService
import com.caioluis.githubpopular.data.impl.BuildConfig
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.local.LocalSourceImpl
import com.caioluis.githubpopular.data.impl.remote.GitHubReposRepositoryImpl
import com.caioluis.githubpopular.data.impl.remote.RemoteSourceImpl
import com.caioluis.githubpopular.data.impl.remote.ServiceBuilder
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindRemoteSource(impl: RemoteSourceImpl): RemoteSource

    @Binds
    fun bindLocalSource(impl: LocalSourceImpl): LocalSource

    @Binds
    fun bindGitHubReposRepository(impl: GitHubReposRepositoryImpl): GitHubReposRepository

    companion object {
        @Provides
        @Singleton
        fun provideGitHubRepositoriesService(): GitHubRepositoriesService =
            ServiceBuilder.Companion<GitHubRepositoriesService>(BuildConfig.API_BASE_URL)

        @Provides
        @Singleton
        fun provideGitHubReposDataBase(
            @ApplicationContext context: Context,
        ): GitHubReposDataBase = GitHubReposDataBase.Companion.getInstance(context)

        @Provides
        fun provideGitHubRepositoriesDao(dataBase: GitHubReposDataBase): GitHubRepositoriesDao = dataBase.gitHubRepositoriesDao()
    }
}
