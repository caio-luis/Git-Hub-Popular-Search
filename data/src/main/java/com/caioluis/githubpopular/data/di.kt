package com.caioluis.githubpopular.data

import com.caioluis.data.BuildConfig
import com.caioluis.githubpopular.data.base.ServiceBuilder
import com.caioluis.githubpopular.data.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.local.LocalSource
import com.caioluis.githubpopular.data.local.LocalSourceImpl
import com.caioluis.githubpopular.data.remote.implementation.GitHubReposRepositoryImpl
import com.caioluis.githubpopular.data.remote.implementation.RemoteSource
import com.caioluis.githubpopular.data.remote.implementation.RemoteSourceImpl
import com.caioluis.githubpopular.data.remote.service.GitHubRepositoriesService
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { ServiceBuilder<GitHubRepositoriesService>(BuildConfig.API_BASE_URL) }
    single { GitHubReposDataBase.getInstance(context = androidContext()) }
    factory { GitHubReposDataBase.getInstance(androidContext()).gitHubRepositoriesDao() }
    factory<RemoteSource> { RemoteSourceImpl(get()) }
    factory<LocalSource> { LocalSourceImpl(get()) }
    factory { GitHubReposDataBase.getInstance(androidContext()).gitHubRepositoriesDao() }
    factory<GitHubReposRepository> { GitHubReposRepositoryImpl(get(), get()) }
}
