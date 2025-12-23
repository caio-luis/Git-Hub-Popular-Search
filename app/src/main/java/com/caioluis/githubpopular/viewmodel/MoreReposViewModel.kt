package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caioluis.githubpopular.domain.bridge.model.Response
import com.caioluis.githubpopular.domain.bridge.usecase.ActualPage
import com.caioluis.githubpopular.domain.bridge.usecase.GetMoreReposUseCase
import com.caioluis.githubpopular.mapper.toUi
import com.caioluis.githubpopular.model.UiGitHubRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MoreReposViewModel(
    private val getMoreReposUseCase: GetMoreReposUseCase,
) : ViewModel() {
    private val moreReposLiveData: MutableLiveData<Response<List<UiGitHubRepository>>> =
        MutableLiveData()
    val observeMoreReposLiveData = moreReposLiveData

    fun loadMore(language: String) {
        viewModelScope.launch {
            runCatching {
                moreReposLiveData.postValue(Response.Loading)
                getMoreReposUseCase
                    .loadRepositories(language)
                    .first()
            }.onSuccess { domainRepositories ->
                ActualPage.increase()
                val uiResponse = domainRepositories?.map { it.toUi() }.orEmpty()
                moreReposLiveData.postValue(Response.Success(uiResponse))
            }.onFailure {
                moreReposLiveData.postValue(Response.Failure(it))
            }
        }
    }
}
