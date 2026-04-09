package com.caioluis.githubpopular.core.common.di

import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.core.common.exception.ErrorMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CoreCommonModule {
    @Binds
    fun bindErrorMapper(impl: ErrorMapperImpl): ErrorMapper
}
