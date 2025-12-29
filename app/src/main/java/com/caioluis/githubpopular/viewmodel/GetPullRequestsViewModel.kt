package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.caioluis.githubpopular.data.GitHubPullRequestsPagingSource
import com.caioluis.githubpopular.domain.bridge.usecase.GetPullRequestsUseCase
import com.caioluis.githubpopular.mapper.toUi
import com.caioluis.githubpopular.model.UiGitHubPullRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetPullRequestsViewModel @Inject constructor(
    private val getPullRequestsUseCase: GetPullRequestsUseCase,
) : ViewModel() {

    private val _pullRequests = MutableStateFlow<PagingData<UiGitHubPullRequest>>(PagingData.empty())
    val pullRequests: StateFlow<PagingData<UiGitHubPullRequest>> = _pullRequests

    fun loadList(pullUrl: String, repositoryId: Int) {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    initialLoadSize = 20,
                    prefetchDistance = 3,
                    enablePlaceholders = true,
                ),
                pagingSourceFactory = {
                    GitHubPullRequestsPagingSource(
                        getPullRequestsUseCase = getPullRequestsUseCase,
                        pullUrl = pullUrl,
                        repositoryId = repositoryId,
                    )
                },
            )
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    val uiPagingData = pagingData.map { domainRepo ->
                        domainRepo.toUi()
                    }
                    _pullRequests.value = uiPagingData
                }
        }
    }
}
