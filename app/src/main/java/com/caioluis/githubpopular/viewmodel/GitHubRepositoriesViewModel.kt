package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.MutableLiveData
import com.caioluis.domain.base.Response
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.usecases.GetRepositoriesUseCase
import com.caioluis.githubpopular.mapper.toUi
import com.caioluis.githubpopular.model.UiGitHubRepository

/**
 * Created by Caio Luis (caio-luis) on 11/10/20
 */

class GitHubRepositoriesViewModel(
    private val getRepositoriesUseCase: GetRepositoriesUseCase
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

    fun loadList(isReloading: Boolean) {
        getRepositoriesUseCase(isReloading)
    }

    override fun onCleared() {
        super.onCleared()
        getRepositoriesUseCase.clear()
    }
}