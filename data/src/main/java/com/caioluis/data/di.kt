package com.caioluis.data

import com.caioluis.data.base.BaseConstants
import com.caioluis.data.base.ServiceBuilder
import com.caioluis.data.local.GitHubReposDataBase
import com.caioluis.data.remote.implementation.GitHubRepositoriesImpl
import com.caioluis.data.remote.service.GitHubRepositoriesService
import com.caioluis.domain.repository.GitHubReposRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by Caio Luis (@caio.luis) on 10/10/20
 */

val dataModule = module {
    single { ServiceBuilder<GitHubRepositoriesService>(BaseConstants.baseUrl) }
    single { GitHubReposDataBase.getInstance(context = androidContext()) }
    factory { GitHubReposDataBase.getInstance(androidContext()).gitHubRepositoriesDao() }
    factory<GitHubReposRepository> { GitHubRepositoriesImpl(get(), get()) }
}