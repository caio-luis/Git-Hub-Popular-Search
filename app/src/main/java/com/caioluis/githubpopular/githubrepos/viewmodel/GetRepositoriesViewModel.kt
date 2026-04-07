package com.caioluis.githubpopular.githubrepos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.githubrepos.GitHubPagingSource
import com.caioluis.githubpopular.githubrepos.mapper.toUi
import com.caioluis.githubpopular.githubrepos.model.UiGitHubRepo
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

    private val _repositories =
        MutableStateFlow<PagingData<UiGitHubRepo>>(PagingData.Companion.empty())
    val repositories: StateFlow<PagingData<UiGitHubRepo>> = _repositories

    private val _selectedLanguage = MutableStateFlow<String?>(null)
    val selectedLanguage: StateFlow<String?> = _selectedLanguage

    fun loadList(language: String) {
        if (_selectedLanguage.value == language) return
        _selectedLanguage.value = language

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
                        domainRepo.toUi()
                    }
                    _repositories.value = uiPagingData
                }
        }
    }
}
