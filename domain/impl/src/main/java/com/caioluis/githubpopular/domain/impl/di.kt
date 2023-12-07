package com.caioluis.githubpopular.domain.impl

import com.caioluis.githubpopular.domain.impl.usecases.GetMoreReposUseCaseImpl
import com.caioluis.githubpopular.domain.impl.usecases.GetRepositoriesUseCaseImpl
import com.caioluis.githubpopular.domain.pub.usecase.GetMoreReposUseCase
import com.caioluis.githubpopular.domain.pub.usecase.GetRepositoriesUseCase
import org.koin.dsl.module

val domainModule = module {
    single<GetRepositoriesUseCase> { GetRepositoriesUseCaseImpl(get()) }
    single<GetMoreReposUseCase> { GetMoreReposUseCaseImpl(get()) }
}
