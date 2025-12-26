package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.mapper.toUi
import com.caioluis.githubpopular.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRepositoriesViewModel @Inject constructor(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var currentPage = 1

    fun loadList(language: String) {
        viewModelScope.launch {
            currentPage = 1

            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    error = null,
                    loadMoreError = null,
                    repositories = emptyList(),
                )
            }

            runCatching {
                getRepositoriesUseCase
                    .loadRepositories(page = currentPage, language = language)
                    .first()
            }.onSuccess { domainRepositories ->
                val uiList = domainRepositories?.map { it.toUi() }.orEmpty()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        repositories = uiList,
                        error = null,
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error,
                    )
                }
            }
        }
    }

    fun loadMore(language: String) {
        if (_uiState.value.isLoadingMore) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, loadMoreError = null) }

            runCatching {
                getRepositoriesUseCase
                    .loadRepositories(page = currentPage + 1, language = language)
                    .first()
            }.onSuccess { domainRepositories ->
                val newItems = domainRepositories?.map { it.toUi() }.orEmpty()

                if (newItems.isNotEmpty()) {
                    currentPage++

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoadingMore = false,
                            repositories = currentState.repositories + newItems,
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoadingMore = false) }
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        loadMoreError = error,
                    )
                }
            }
        }
    }
}
