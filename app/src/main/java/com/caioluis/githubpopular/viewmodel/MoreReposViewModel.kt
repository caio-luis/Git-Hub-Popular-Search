package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caioluis.githubpopular.domain.bridge.base.Response
import com.caioluis.githubpopular.impl.usecases.ActualPage
import com.caioluis.githubpopular.impl.usecases.GetMoreReposUseCase
import com.caioluis.githubpopular.mapper.toUi
import com.caioluis.githubpopular.model.UiGitHubRepository
import kotlinx.coroutines.launch

class MoreReposViewModel(
    private val getMoreReposUseCase: GetMoreReposUseCase,
) : ViewModel() {

    private val moreReposLiveData: MutableLiveData<Response<List<UiGitHubRepository>>> =
        MutableLiveData()
    val observeMoreReposLiveData = moreReposLiveData

    fun loadMore(language: String) {
        viewModelScope.launch {
            moreReposLiveData.postValue(Response.Loading)

            getMoreReposUseCase
                .loadRepositories(language)
                .onSuccess { domainRepositories ->
                    val uiResponse = domainRepositories?.map { it.toUi() }.orEmpty()
                    moreReposLiveData.postValue(Response.Success(uiResponse))

                    ActualPage.increase()
                }
                .onFailure { moreReposLiveData.postValue(Response.Failure(it)) }
        }
    }
}
