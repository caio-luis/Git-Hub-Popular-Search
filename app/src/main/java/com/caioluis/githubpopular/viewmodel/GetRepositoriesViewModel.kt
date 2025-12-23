package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caioluis.githubpopular.domain.bridge.model.Response
import com.caioluis.githubpopular.domain.bridge.usecase.ActualPage
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.mapper.toUi
import com.caioluis.githubpopular.model.UiGitHubRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GetRepositoriesViewModel(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
) : ViewModel() {
    private val gitHubReposLiveData: MutableLiveData<Response<List<UiGitHubRepository>>> =
        MutableLiveData()
    val observeGitHubReposLiveData = gitHubReposLiveData

    fun loadList(language: String) {
        viewModelScope.launch {
            runCatching {
                ActualPage.reset()
                gitHubReposLiveData.postValue(Response.Loading)
                getRepositoriesUseCase
                    .loadRepositories(language)
                    .first()
            }.onSuccess { domainRepositories ->
                ActualPage.increase()
                val uiResponse = domainRepositories?.map { it.toUi() }.orEmpty()
                gitHubReposLiveData.postValue(Response.Success(uiResponse))
            }.onFailure {
                gitHubReposLiveData.postValue(Response.Failure(it))
            }
        }
    }
}
