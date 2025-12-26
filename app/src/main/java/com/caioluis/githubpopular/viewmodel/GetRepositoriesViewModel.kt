package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.caioluis.githubpopular.data.GitHubPagingSource
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.model.UiGitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRepositoriesViewModel @Inject constructor(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
) : ViewModel() {

    private val _repositories = MutableStateFlow<PagingData<UiGitHubRepository>>(PagingData.empty())
    val repositories: StateFlow<PagingData<UiGitHubRepository>> = _repositories

    fun loadList(language: String) {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    initialLoadSize = 20,
                    prefetchDistance = 3,
                    enablePlaceholders = true,
                ),
                pagingSourceFactory = {
                    GitHubPagingSource(
                        getRepositoriesUseCase = getRepositoriesUseCase,
                        language = language,
                    )
                },
            )
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    val uiPagingData = pagingData.map { domainRepo ->
                        UiGitHubRepository(
                            id = domainRepo.id,
                            name = domainRepo.name,
                            description = domainRepo.description,
                            forksCount = domainRepo.forksCount,
                            stargazersCount = domainRepo.stargazersCount,
                            owner = domainRepo.owner.let { owner ->
                                com.caioluis.githubpopular.model.UiRepositoryOwner(
                                    avatarUrl = owner.avatarUrl,
                                    login = owner.login,
                                )
                            },
                        )
                    }
                    _repositories.value = uiPagingData
                }
        }
    }
}
