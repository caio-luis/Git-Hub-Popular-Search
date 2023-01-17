package com.caioluis.githubpopular.base

import com.caioluis.githubpopular.viewmodel.GitHubRepositoriesViewModel
import com.caioluis.githubpopular.viewmodel.MoreReposViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Caio Luis (caio-luis) on 12/10/20
 */

val viewModelModule = module {
    viewModel { GitHubRepositoriesViewModel(get()) }
    viewModel { MoreReposViewModel(get()) }
}