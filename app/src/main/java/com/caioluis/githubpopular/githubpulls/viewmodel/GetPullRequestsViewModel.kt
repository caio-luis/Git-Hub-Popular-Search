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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import timber.log.Timber
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

    private val requestState = MutableStateFlow(
        pullRequestsRequestFromState(),
    )

    val repositoryName: StateFlow<String> = savedStateHandle.getStateFlow(REPOSITORY_NAME_KEY, "")

    val currentRepositoryId: StateFlow<Int?> =
        savedStateHandle.getStateFlow(REPOSITORY_ID_KEY, null)

    val pullRequests: Flow<PagingData<UiGitHubPullRequest>> =
        requestState
            .filterNotNull()
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
        Timber.d("loadList triggered: repositoryId=%d, repositoryName=%s, pullUrl=%s", repositoryId, repositoryName, pullUrl)
        savedStateHandle[PULL_URL_KEY] = pullUrl
        savedStateHandle[REPOSITORY_ID_KEY] = repositoryId
        if (repositoryName != null) {
            savedStateHandle[REPOSITORY_NAME_KEY] = repositoryName
        }

        requestState.value = PullRequestsRequest(
            pullUrl = pullUrl,
            repositoryId = repositoryId,
        )
    }

    fun mapToAppException(error: Throwable): AppException = errorMapper.map(error)

    private fun pullRequestsRequestFromState(): PullRequestsRequest? {
        val pullUrl = savedStateHandle.get<String>(PULL_URL_KEY)
        val repositoryId = savedStateHandle.get<Int>(REPOSITORY_ID_KEY)

        return if (pullUrl != null && repositoryId != null) {
            PullRequestsRequest(
                pullUrl = pullUrl,
                repositoryId = repositoryId,
            )
        } else {
            null
        }
    }

    private companion object {
        const val PULL_URL_KEY = "pullUrl"
        const val REPOSITORY_ID_KEY = "repositoryId"
        const val REPOSITORY_NAME_KEY = "repositoryName"
    }
}
