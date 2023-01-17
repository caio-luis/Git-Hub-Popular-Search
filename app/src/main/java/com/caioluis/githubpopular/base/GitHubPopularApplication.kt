package com.caioluis.githubpopular.base

import android.app.Application
import com.caioluis.data.dataModule
import com.caioluis.domain.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by Caio Luis (caio-luis) on 10/10/20
 */
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