package com.caioluis.githubpopular.di

import com.caioluis.githubpopular.githubrepos.mapper.UiGitHubRepoMapper
import com.caioluis.githubpopular.githubrepos.mapper.UiGitHubRepoMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Binds
    fun bindsUiGitHubRepoMapper(impl: UiGitHubRepoMapperImpl): UiGitHubRepoMapper
}
