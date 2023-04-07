package com.caioluis.data

import com.caioluis.data.base.BaseConstants
import com.caioluis.data.base.ServiceBuilder
import com.caioluis.data.local.GitHubReposDataBase
import com.caioluis.data.local.LocalSource
import com.caioluis.data.local.LocalSourceImpl
import com.caioluis.data.remote.implementation.GitHubReposRepositoryImpl
import com.caioluis.data.remote.implementation.RemoteSource
import com.caioluis.data.remote.implementation.RemoteSourceImpl
import com.caioluis.data.remote.service.GitHubRepositoriesService
import com.caioluis.domain.repository.GitHubReposRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { ServiceBuilder<GitHubRepositoriesService>(BaseConstants.baseUrl) }
    single { GitHubReposDataBase.getInstance(context = androidContext()) }
    factory { GitHubReposDataBase.getInstance(androidContext()).gitHubRepositoriesDao() }
    factory<RemoteSource> { RemoteSourceImpl(get()) }
    factory<LocalSource> { LocalSourceImpl(get()) }
    factory { GitHubReposDataBase.getInstance(androidContext()).gitHubRepositoriesDao() }
    factory<GitHubReposRepository> { GitHubReposRepositoryImpl(get(), get()) }
}
