package com.caioluis.githubpopular

import com.caioluis.githubpopular.viewmodel.GetRepositoriesViewModel
import com.caioluis.githubpopular.viewmodel.MoreReposViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GetRepositoriesViewModel(get()) }
    viewModel { MoreReposViewModel(get()) }
}
