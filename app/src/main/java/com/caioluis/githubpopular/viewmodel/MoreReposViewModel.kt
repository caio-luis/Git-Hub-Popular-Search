package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.MutableLiveData
import com.caioluis.githubpopular.base.BaseViewModel
import com.caioluis.githubpopular.domain.bridge.base.Response
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.impl.usecases.GetMoreReposUseCase
import com.caioluis.githubpopular.mapper.toUi
import com.caioluis.githubpopular.model.UiGitHubRepository

class MoreReposViewModel(
    private val getMoreReposUseCase: GetMoreReposUseCase,
) : BaseViewModel<List<DomainGitHubRepository>>(getMoreReposUseCase.receiveChannel) {

    private val moreReposLiveData: MutableLiveData<Response<List<UiGitHubRepository>>> =
        MutableLiveData()
    val observeMoreReposLiveData = moreReposLiveData

    override fun handle(response: Response<List<DomainGitHubRepository>>) {

        moreReposLiveData.apply {

            response.handleResponse(
                onLoading = { postValue(Response.Loading) },

                onSuccess = { domainRepositories ->
                    val uiResponse = domainRepositories.map { it.toUi() }
                    postValue(Response.Success(uiResponse))
                },

                onFailure = { postValue(Response.Failure(it)) }
            )
        }
    }

    fun loadMore(language: String) {
        getMoreReposUseCase(language)
    }

    override fun onCleared() {
        super.onCleared()
        getMoreReposUseCase.clear()
    }
}
