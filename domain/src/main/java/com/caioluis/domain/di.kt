package com.caioluis.domain

import com.caioluis.domain.usecases.GetMoreReposUseCase
import com.caioluis.domain.usecases.GetRepositoriesUseCase
import org.koin.dsl.module

/**
 * Created by Caio Luis (caio-luis) on 11/10/20
 */

val domainModule = module {
    single { GetRepositoriesUseCase(get()) }
    single { GetMoreReposUseCase(get()) }
}
