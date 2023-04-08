package com.caioluis.githubpopular.base

import android.app.Application
import com.caioluis.data.dataModule
import com.caioluis.domain.domainModule
import com.caioluis.githubpopular.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GitHubPopularApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GitHubPopularApplication)
            modules(
                listOf(
                    dataModule,
                    domainModule,
                    viewModelModule,
                )
            )
        }
    }
}
