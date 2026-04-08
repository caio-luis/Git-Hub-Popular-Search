package com.caioluis.githubpopular.githubrepos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.githubrepos.mapper.UiGitHubRepoMapper
import com.caioluis.githubpopular.githubrepos.model.UiGitHubRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class GetRepositoriesViewModel @Inject constructor(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
    private val domainMapper: UiGitHubRepoMapper,
) : ViewModel() {
    private val _selectedLanguage = MutableStateFlow<String?>(null)
    val selectedLanguage: StateFlow<String?> = _selectedLanguage.asStateFlow()

    val repositories: Flow<PagingData<UiGitHubRepo>> = _selectedLanguage
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { language ->
            getRepositoriesUseCase.loadRepositories(language)
                .map { pagingData ->
                    pagingData.map(domainMapper::mapToUi)
                }
        }
        .cachedIn(viewModelScope)

    fun loadList(language: String) {
        _selectedLanguage.value = language
    }
}
