package com.caioluis.githubpopular

import com.caioluis.githubpopular.viewmodel.GitHubRepositoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Caio Luis (@caio.luis) on 12/10/20
 */

val viewModelModule = module {
    viewModel { GitHubRepositoriesViewModel(get()) }
}