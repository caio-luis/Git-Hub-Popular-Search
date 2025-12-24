package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caioluis.githubpopular.domain.bridge.model.Response
import com.caioluis.githubpopular.domain.bridge.usecase.ActualPage
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.mapper.toUi
import com.caioluis.githubpopular.model.UiGitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRepositoriesViewModel
@Inject
constructor(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
) : ViewModel() {
    private val allRepositories = mutableListOf<UiGitHubRepository>()

    private val _repositoriesFlow =
        MutableStateFlow<Response<List<UiGitHubRepository>>>(Response.Success(emptyList()))
    val repositoriesFlow = _repositoriesFlow.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore.asStateFlow()

    private val _loadMoreError = MutableStateFlow<Throwable?>(null)
    val loadMoreError = _loadMoreError.asStateFlow()

    fun loadList(language: String) {
        viewModelScope.launch {
            ActualPage.reset()
            _repositoriesFlow.value = Response.Loading
            allRepositories.clear()
            _loadMoreError.value = null
            _isLoadingMore.value = false

            runCatching {
                getRepositoriesUseCase
                    .loadRepositories(language)
                    .first()
            }.onSuccess { domainRepositories ->
                ActualPage.increase()
                val uiResponse = domainRepositories?.map { it.toUi() }.orEmpty()
                allRepositories.addAll(uiResponse)
                _repositoriesFlow.value = Response.Success(allRepositories.toList())
            }.onFailure {
                _repositoriesFlow.value = Response.Failure(it)
            }
        }
    }

    fun loadMore(language: String) {
        if (_isLoadingMore.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            _loadMoreError.value = null

            runCatching {
                getRepositoriesUseCase
                    .loadRepositories(language)
                    .first()
            }.onSuccess { domainRepositories ->
                ActualPage.increase()
                val uiResponse = domainRepositories?.map { it.toUi() }.orEmpty()
                if (uiResponse.isNotEmpty()) {
                    allRepositories.addAll(uiResponse)
                    _repositoriesFlow.value = Response.Success(allRepositories.toList())
                }
                _isLoadingMore.value = false
            }.onFailure {
                _loadMoreError.value = it
                _isLoadingMore.value = false
            }
        }
    }
}
