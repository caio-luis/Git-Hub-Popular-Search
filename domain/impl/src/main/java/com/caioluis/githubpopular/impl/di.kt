package com.caioluis.githubpopular.impl

import com.caioluis.githubpopular.impl.usecases.GetMoreReposUseCase
import com.caioluis.githubpopular.impl.usecases.GetRepositoriesUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GetRepositoriesUseCase(get()) }
    single { GetMoreReposUseCase(get()) }
}
