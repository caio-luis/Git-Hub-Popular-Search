package com.caioluis.githubpopular.data.impl.di

import ServiceBuilder
import android.content.Context
import com.caioluis.githubpopular.data.impl.BuildConfig
import com.caioluis.githubpopular.data.impl.local.githubpullrequests.PullRequestsLocalSource
import com.caioluis.githubpopular.data.impl.local.githubpullrequests.PullRequestsLocalSourceImpl
import com.caioluis.githubpopular.data.impl.local.githubpullrequests.dao.GitHubPullRequestsDao
import com.caioluis.githubpopular.data.impl.local.githubrepos.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.local.githubrepos.LocalSource
import com.caioluis.githubpopular.data.impl.local.githubrepos.LocalSourceImpl
import com.caioluis.githubpopular.data.impl.local.githubrepos.dao.GitHubRepositoriesDao
import com.caioluis.githubpopular.data.impl.remote.GitHubReposRepositoryImpl
import com.caioluis.githubpopular.data.impl.remote.PullRequestsRemoteSource
import com.caioluis.githubpopular.data.impl.remote.PullRequestsRemoteSourceImpl
import com.caioluis.githubpopular.data.impl.remote.RemoteSource
import com.caioluis.githubpopular.data.impl.remote.RemoteSourceImpl
import com.caioluis.githubpopular.data.impl.remote.service.GitHubPullRequestsService
import com.caioluis.githubpopular.data.impl.remote.service.GitHubRepositoriesService
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
    fun bindPullRequestsLocalSource(impl: PullRequestsLocalSourceImpl): PullRequestsLocalSource

    @Binds
    fun bindPullRequestsRemoteSource(impl: PullRequestsRemoteSourceImpl): PullRequestsRemoteSource

    @Binds
    fun bindGitHubReposRepository(impl: GitHubReposRepositoryImpl): GitHubReposRepository

    companion object {
        @Provides
        @Singleton
        fun provideGitHubRepositoriesService(): GitHubRepositoriesService = ServiceBuilder.Companion<GitHubRepositoriesService>(BuildConfig.API_BASE_URL)

        @Provides
        @Singleton
        fun provideGitHubPullRequestsService(): GitHubPullRequestsService = ServiceBuilder.Companion<GitHubPullRequestsService>(BuildConfig.API_BASE_URL)

        @Provides
        @Singleton
        fun provideGitHubReposDataBase(
            @ApplicationContext context: Context,
        ): GitHubReposDataBase = GitHubReposDataBase.getInstance(context)

        @Provides
        fun provideGitHubRepositoriesDao(dataBase: GitHubReposDataBase): GitHubRepositoriesDao = dataBase.gitHubRepositoriesDao()

        @Provides
        fun provideGitHubPullRequestsDao(dataBase: GitHubReposDataBase): GitHubPullRequestsDao = dataBase.gitHubPullRequestsDao()
    }
}
