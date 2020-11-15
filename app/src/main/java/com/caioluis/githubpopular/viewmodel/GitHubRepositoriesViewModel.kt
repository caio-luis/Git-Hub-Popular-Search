package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.MutableLiveData
import com.caioluis.domain.base.Response
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.usecases.GetRepositoriesUseCase
import com.caioluis.githubpopular.mapper.toUi
import com.caioluis.githubpopular.model.UiGitHubRepository

/**
 * Created by Caio Luis (@caio.luis) on 11/10/20
 */

class GitHubRepositoriesViewModel(
    private val getRepositoriesUseCase: GetRepositoriesUseCase
) : BaseViewModel<List<DomainGitHubRepository>>(getRepositoriesUseCase.receiveChannel) {

    var pageNumber = 1

    private val gitHubReposLiveData: MutableLiveData<Response<List<UiGitHubRepository>>> =
        MutableLiveData()
    val observeGitHubReposLiveData = gitHubReposLiveData

    override fun handle(response: Response<List<DomainGitHubRepository>>) {

        response.handleResponse(
            onLoading = { gitHubReposLiveData.postValue(Response.Loading) },

            onSuccess = { domainRepositories ->
                val uiResponse = domainRepositories.map { it.toUi() }
                gitHubReposLiveData.postValue(Response.Success(uiResponse))
            },

            onFailure = { gitHubReposLiveData.postValue(Response.Failure(it)) }
        )
    }

    fun fetchRepositories() {
        getRepositoriesUseCase.invoke(pageNumber)
    }

    override fun onCleared() {
        super.onCleared()
        pageNumber = 1
    }
}