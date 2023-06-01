package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.MutableLiveData
import com.caioluis.githubpopular.base.BaseViewModel
import com.caioluis.githubpopular.domain.bridge.base.Response
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.impl.usecases.GetRepositoriesUseCase
import com.caioluis.githubpopular.mapper.toUi
import com.caioluis.githubpopular.model.UiGitHubRepository

class GitHubRepositoriesViewModel(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
) : BaseViewModel<List<DomainGitHubRepository>>(getRepositoriesUseCase.receiveChannel) {

    private val gitHubReposLiveData: MutableLiveData<Response<List<UiGitHubRepository>>> =
        MutableLiveData()
    val observeGitHubReposLiveData = gitHubReposLiveData

    override fun handle(response: Response<List<DomainGitHubRepository>>) {

        gitHubReposLiveData.apply {

            response.handleResponse(
                onLoading = { postValue(Response.Loading) },

                onSuccess = { domainRepositories ->
                    val uiResponse = domainRepositories.map { it.toUi() }
                    postValue(Response.Success(uiResponse))
                },

                onFailure = {
                    postValue(Response.Failure(it))
                }
            )
        }
    }

    fun loadList(language: String) {
        getRepositoriesUseCase(language)
    }

    override fun onCleared() {
        getRepositoriesUseCase.clear()
        super.onCleared()
    }
}
