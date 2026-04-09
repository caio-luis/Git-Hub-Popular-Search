package com.caioluis.githubpopular.domain.impl.di

import com.caioluis.githubpopular.domain.bridge.usecase.GetPullRequestsUseCase
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.domain.impl.usecases.GetPullRequestsUseCaseImpl
import com.caioluis.githubpopular.domain.impl.usecases.GetRepositoriesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {
    @Binds
    @Singleton
    fun bindGetRepositoriesUseCase(impl: GetRepositoriesUseCaseImpl): GetRepositoriesUseCase

    @Binds
    @Singleton
    fun bindGetPullRequestsUseCase(impl: GetPullRequestsUseCaseImpl): GetPullRequestsUseCase
}
