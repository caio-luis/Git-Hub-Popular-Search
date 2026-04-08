package com.caioluis.githubpopular.data.impl.di

import android.content.Context
import com.caioluis.githubpopular.core.common.ServiceBuilder
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.core.common.exception.ErrorMapperImpl
import com.caioluis.githubpopular.data.impl.BuildConfig
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.local.githubpulls.PullRequestsLocalSource
import com.caioluis.githubpopular.data.impl.local.githubpulls.PullRequestsLocalSourceImpl
import com.caioluis.githubpopular.data.impl.local.githubpulls.dao.GitHubPullRequestsDao
import com.caioluis.githubpopular.data.impl.local.githubrepos.dao.GitHubRepositoriesDao
import com.caioluis.githubpopular.data.impl.remote.githubpullrequests.PullRequestsRemoteSource
import com.caioluis.githubpopular.data.impl.remote.githubpullrequests.PullRequestsRemoteSourceImpl
import com.caioluis.githubpopular.data.impl.remote.githubpullrequests.repository.GitHubPullRequestsRepositoryImpl
import com.caioluis.githubpopular.data.impl.remote.githubpullrequests.service.GitHubPullRequestsService
import com.caioluis.githubpopular.data.impl.remote.githubrepos.GithubReposRemoteSource
import com.caioluis.githubpopular.data.impl.remote.githubrepos.GithubReposRemoteSourceImpl
import com.caioluis.githubpopular.data.impl.remote.githubrepos.repository.GitHubReposRepositoryImpl
import com.caioluis.githubpopular.data.impl.remote.githubrepos.service.GitHubRepositoriesService
import com.caioluis.githubpopular.domain.bridge.repository.GitHubPullRequestsRepository
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
    fun bindRemoteSource(impl: GithubReposRemoteSourceImpl): GithubReposRemoteSource

    @Binds
    fun bindPullRequestsLocalSource(impl: PullRequestsLocalSourceImpl): PullRequestsLocalSource

    @Binds
    fun bindPullRequestsRemoteSource(impl: PullRequestsRemoteSourceImpl): PullRequestsRemoteSource

    @Binds
    fun bindGitHubReposRepository(impl: GitHubReposRepositoryImpl): GitHubReposRepository

    @Binds
    fun bindGitHubPullRequestsRepository(impl: GitHubPullRequestsRepositoryImpl): GitHubPullRequestsRepository

    @Binds
    fun bindErrorMapper(impl: ErrorMapperImpl): ErrorMapper

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
