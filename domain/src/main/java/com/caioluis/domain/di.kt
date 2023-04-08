package com.caioluis.domain

import com.caioluis.domain.usecases.GetMoreReposUseCase
import com.caioluis.domain.usecases.GetRepositoriesUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GetRepositoriesUseCase(get()) }
    single { GetMoreReposUseCase(get()) }
}
