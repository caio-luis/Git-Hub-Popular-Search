package com.caioluis.githubpopular.domain.impl.di

import com.caioluis.githubpopular.domain.bridge.usecase.GetMoreReposUseCase
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.domain.impl.usecases.GetMoreReposUseCaseImpl
import com.caioluis.githubpopular.domain.impl.usecases.GetRepositoriesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {
    @Binds
    fun bindGetRepositoriesUseCase(impl: GetRepositoriesUseCaseImpl): GetRepositoriesUseCase

    @Binds
    fun bindGetMoreReposUseCase(impl: GetMoreReposUseCaseImpl): GetMoreReposUseCase
}
