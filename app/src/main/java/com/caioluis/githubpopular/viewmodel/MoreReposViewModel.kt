package com.caioluis.githubpopular.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caioluis.githubpopular.Constants.VIEW_MODELS_ERROR_TAG
import com.caioluis.githubpopular.domain.bridge.model.Response
import com.caioluis.githubpopular.domain.bridge.usecase.GetMoreReposUseCase
import com.caioluis.githubpopular.impl.usecases.ActualPage
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
                .onFailure {
                    moreReposLiveData.postValue(Response.Failure(it))
                    Log.e(VIEW_MODELS_ERROR_TAG, it.message.orEmpty())
                }
        }
    }
}
