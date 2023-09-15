package com.caioluis.githubpopular.domain.impl

import com.caioluis.githubpopular.domain.bridge.usecase.GetMoreReposUseCase
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.domain.impl.usecases.GetMoreReposUseCaseImpl
import com.caioluis.githubpopular.domain.impl.usecases.GetRepositoriesUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    single<GetRepositoriesUseCase> { GetRepositoriesUseCaseImpl(get()) }
    single<GetMoreReposUseCase> { GetMoreReposUseCaseImpl(get()) }
}
