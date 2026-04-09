package com.caioluis.githubpopular.githubpulls.viewmodel

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class GetPullRequestsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPullRequestsUseCase: GetPullRequestsUseCase,
    private val pullRequestUiMapper: PullRequestUiMapper,
    private val errorMapper: ErrorMapper,
) : ViewModel() {
    private data class PullRequestsRequest(
        val pullUrl: String,
        val repositoryId: Int,
    )

    private val pullUrl = savedStateHandle.getStateFlow<String?>(PULL_URL_KEY, null)
    private val repositoryId = savedStateHandle.getStateFlow<Int?>(REPOSITORY_ID_KEY, null)

    val repositoryName: String = savedStateHandle.get<String>(REPOSITORY_NAME_KEY).orEmpty()
    val currentRepositoryId: Int? get() = repositoryId.value

    val pullRequests: Flow<PagingData<UiGitHubPullRequest>> = combine(pullUrl, repositoryId) { currentPullUrl, currentRepositoryId ->
        if (currentPullUrl == null || currentRepositoryId == null) {
            null
        } else {
            PullRequestsRequest(
                pullUrl = currentPullUrl,
                repositoryId = currentRepositoryId,
            )
        }
    }
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

    fun loadList(pullUrl: String, repositoryId: Int, repositoryName: String? = null) {
        savedStateHandle[PULL_URL_KEY] = pullUrl
        savedStateHandle[REPOSITORY_ID_KEY] = repositoryId
        if (repositoryName != null) {
            savedStateHandle[REPOSITORY_NAME_KEY] = repositoryName
        }
    }

    fun mapToAppException(error: Throwable): AppException = (error as? AppException) ?: errorMapper.map(error)

    private companion object {
        const val PULL_URL_KEY = "pullUrl"
        const val REPOSITORY_ID_KEY = "repositoryId"
        const val REPOSITORY_NAME_KEY = "repositoryName"
    }
}
