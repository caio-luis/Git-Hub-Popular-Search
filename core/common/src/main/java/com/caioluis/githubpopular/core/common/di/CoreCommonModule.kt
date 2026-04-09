package com.caioluis.githubpopular.core.common.di

import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.core.common.exception.ErrorMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CoreCommonModule {
    @Binds
    @Singleton
    fun bindErrorMapper(impl: ErrorMapperImpl): ErrorMapper
}
