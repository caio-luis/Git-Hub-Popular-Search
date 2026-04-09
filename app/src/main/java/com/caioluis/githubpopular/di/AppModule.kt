package com.caioluis.githubpopular.di

import com.caioluis.githubpopular.githubpulls.mapper.PullRequestUiMapper
import com.caioluis.githubpopular.githubpulls.mapper.PullRequestUiMapperImpl
import com.caioluis.githubpopular.githubrepos.mapper.UiGitHubRepoMapper
import com.caioluis.githubpopular.githubrepos.mapper.UiGitHubRepoMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Binds
    @Singleton
    fun bindsUiGitHubRepoMapper(impl: UiGitHubRepoMapperImpl): UiGitHubRepoMapper

    @Binds
    @Singleton
    fun bindsPullRequestUiMapper(impl: PullRequestUiMapperImpl): PullRequestUiMapper
}
