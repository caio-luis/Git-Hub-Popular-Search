package com.caioluis.githubpopular.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caioluis.githubpopular.Constants.VIEW_MODELS_ERROR_TAG
import com.caioluis.githubpopular.domain.bridge.model.Response
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.domain.impl.usecases.ActualPage
import com.caioluis.githubpopular.mapper.toUi
import com.caioluis.githubpopular.model.UiGitHubRepository
import kotlinx.coroutines.launch

class GetRepositoriesViewModel(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
) : ViewModel() {

    private val gitHubReposLiveData: MutableLiveData<Response<List<UiGitHubRepository>>> =
        MutableLiveData()
    val observeGitHubReposLiveData = gitHubReposLiveData

    fun loadList(language: String) {

        viewModelScope.launch {
            gitHubReposLiveData.postValue(Response.Loading)

            getRepositoriesUseCase.loadRepositories(language)
                .onSuccess { domainRepositories ->
                    val uiResponse = domainRepositories?.map { it.toUi() }.orEmpty()
                    gitHubReposLiveData.postValue(Response.Success(uiResponse))

                    ActualPage.increase()
                }
                .onFailure {
                    gitHubReposLiveData.postValue(Response.Failure(it))
                    Log.e(VIEW_MODELS_ERROR_TAG, it.message.orEmpty())
                }
        }
    }
}
