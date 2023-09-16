package com.caioluis.githubpopular.impl

import com.caioluis.githubpopular.data.bridge.local.LocalSource
import com.caioluis.githubpopular.data.bridge.remote.RemoteSource
import com.caioluis.githubpopular.data.bridge.remote.service.GitHubRepositoriesService
import com.caioluis.githubpopular.data.impl.BuildConfig
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import com.caioluis.githubpopular.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.impl.local.LocalSourceImpl
import com.caioluis.githubpopular.impl.remote.GitHubReposRepositoryImpl
import com.caioluis.githubpopular.impl.remote.RemoteSourceImpl
import com.caioluis.githubpopular.impl.remote.ServiceBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { ServiceBuilder<GitHubRepositoriesService>(BuildConfig.API_BASE_URL) }
    single { GitHubReposDataBase.getInstance(context = androidContext()) }
    factory {
        GitHubReposDataBase.getInstance(androidContext()).gitHubRepositoriesDao()
    }
    factory<RemoteSource> { RemoteSourceImpl(get()) }
    factory<LocalSource> { LocalSourceImpl(get()) }
    factory {
        GitHubReposDataBase.getInstance(androidContext()).gitHubRepositoriesDao()
    }
    factory<GitHubReposRepository> { GitHubReposRepositoryImpl(get(), get()) }
}
