package com.caioluis.githubpopular.data.impl.di

import android.content.Context
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.local.githubpulls.dao.GitHubPullRequestsDao
import com.caioluis.githubpopular.data.impl.local.githubrepos.dao.GitHubRepositoriesDao
import com.caioluis.githubpopular.data.impl.mapper.LocalGitHubPullRequestMapper
import com.caioluis.githubpopular.data.impl.mapper.LocalGitHubPullRequestMapperImpl
import com.caioluis.githubpopular.data.impl.mapper.LocalGitHubRepositoryMapper
import com.caioluis.githubpopular.data.impl.mapper.LocalGitHubRepositoryMapperImpl
import com.caioluis.githubpopular.data.impl.mapper.RemoteGitHubRepositoryMapper
import com.caioluis.githubpopular.data.impl.mapper.RemoteGitHubRepositoryMapperImpl
import com.caioluis.githubpopular.data.impl.mapper.RemotePullRequestMapper
import com.caioluis.githubpopular.data.impl.mapper.RemotePullRequestMapperImpl
import com.caioluis.githubpopular.data.impl.remote.githubpullrequests.PullRequestsRemoteSource
import com.caioluis.githubpopular.data.impl.remote.githubpullrequests.PullRequestsRemoteSourceImpl
import com.caioluis.githubpopular.data.impl.remote.githubpulls.repository.GitHubPullRequestsRepositoryImpl
import com.caioluis.githubpopular.data.impl.remote.githubpulls.service.GitHubPullRequestsService
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
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindRemoteSource(impl: GithubReposRemoteSourceImpl): GithubReposRemoteSource

    @Binds
    fun bindPullRequestsRemoteSource(impl: PullRequestsRemoteSourceImpl): PullRequestsRemoteSource

    @Binds
    @Singleton
    fun bindGitHubReposRepository(impl: GitHubReposRepositoryImpl): GitHubReposRepository

    @Binds
    @Singleton
    fun bindGitHubPullRequestsRepository(impl: GitHubPullRequestsRepositoryImpl): GitHubPullRequestsRepository

    @Binds
    fun bindRemoteGitHubRepositoryMapper(impl: RemoteGitHubRepositoryMapperImpl): RemoteGitHubRepositoryMapper

    @Binds
    fun bindRemotePullRequestMapper(impl: RemotePullRequestMapperImpl): RemotePullRequestMapper

    @Binds
    fun bindLocalGitHubRepositoryMapper(impl: LocalGitHubRepositoryMapperImpl): LocalGitHubRepositoryMapper

    @Binds
    fun bindLocalGitHubPullRequestMapper(impl: LocalGitHubPullRequestMapperImpl): LocalGitHubPullRequestMapper

    companion object {
        @Provides
        @Singleton
        fun provideGitHubRepositoriesService(
            retrofit: Retrofit,
        ): GitHubRepositoriesService = retrofit.create(GitHubRepositoriesService::class.java)

        @Provides
        @Singleton
        fun provideGitHubPullRequestsService(
            retrofit: Retrofit,
        ): GitHubPullRequestsService = retrofit.create(GitHubPullRequestsService::class.java)

        @Provides
        @Singleton
        fun provideGitHubReposDataBase(
            @ApplicationContext context: Context,
        ): GitHubReposDataBase = GitHubReposDataBase.getInstance(context)

        @Provides
        @Singleton
        fun provideGitHubRepositoriesDao(dataBase: GitHubReposDataBase): GitHubRepositoriesDao = dataBase.gitHubRepositoriesDao()

        @Provides
        @Singleton
        fun provideGitHubPullRequestsDao(dataBase: GitHubReposDataBase): GitHubPullRequestsDao = dataBase.gitHubPullRequestsDao()
    }
}
