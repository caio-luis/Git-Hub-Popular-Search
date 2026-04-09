package com.caioluis.githubpopular.githubpulls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.caioluis.githubpopular.core.common.exception.AppException
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.domain.bridge.usecase.GetPullRequestsUseCase
import com.caioluis.githubpopular.githubpulls.mapper.PullRequestUiMapper
import com.caioluis.githubpopular.githubpulls.model.UiGitHubPullRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class GetPullRequestsViewModel @Inject constructor(
    private val getPullRequestsUseCase: GetPullRequestsUseCase,
    private val pullRequestUiMapper: PullRequestUiMapper,
    private val errorMapper: ErrorMapper,
) : ViewModel() {
    private data class PullRequestsRequest(
        val pullUrl: String,
        val repositoryId: Int,
    )

    private val selectedRequest = MutableStateFlow<PullRequestsRequest?>(null)

    val pullRequests: Flow<PagingData<UiGitHubPullRequest>> = selectedRequest
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { request ->
            getPullRequestsUseCase.loadPullRequests(
                pullUrl = request.pullUrl,
                repositoryId = request.repositoryId,
            ).map { pagingData ->
                pagingData.map(pullRequestUiMapper::mapToUi)
            }
        }
        .cachedIn(viewModelScope)

    fun loadList(pullUrl: String, repositoryId: Int) {
        selectedRequest.value = PullRequestsRequest(
            pullUrl = pullUrl,
            repositoryId = repositoryId,
        )
    }

    fun mapToAppException(error: Throwable): AppException = (error as? AppException) ?: errorMapper.map(error)
}
