package com.caioluis.data

import com.caioluis.data.remote.GitHubRepositoriesService
import com.caioluis.data.remote.ServiceBuilder
import com.caioluis.data.remote.implementation.GitHubRepositoriesImpl
import com.caioluis.domain.repository.GitHubReposRepository
import org.koin.dsl.module

/**
 * Created by Caio Luis (@caio.luis) on 10/10/20
 */

val dataModule = module {
    single { ServiceBuilder<GitHubRepositoriesService>(BaseConstants.baseUrl) }
    factory<GitHubReposRepository> { GitHubRepositoriesImpl(get()) }
}