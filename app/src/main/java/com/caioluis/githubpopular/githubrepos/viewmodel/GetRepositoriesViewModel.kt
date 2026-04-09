package com.caioluis.githubpopular.githubrepos.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.caioluis.githubpopular.Constants
import com.caioluis.githubpopular.core.common.exception.AppException
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.githubrepos.mapper.UiGitHubRepoMapper
import com.caioluis.githubpopular.githubrepos.model.UiGitHubRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class GetRepositoriesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
    private val domainMapper: UiGitHubRepoMapper,
    private val errorMapper: ErrorMapper,
) : ViewModel() {
    val selectedLanguage: StateFlow<String?> = savedStateHandle.getStateFlow(
        key = SELECTED_LANGUAGE_KEY,
        initialValue = null,
    )

    val repositories: Flow<PagingData<UiGitHubRepo>> = selectedLanguage
        .filterNotNull()
        .flatMapLatest { language ->
            getRepositoriesUseCase.loadRepositories(language)
                .map { pagingData ->
                    pagingData.map(domainMapper::mapToUi)
                }
        }
        .cachedIn(viewModelScope)

    fun loadList(language: String) {
        Timber.d("loadList triggered: language=%s", language)
        savedStateHandle[SELECTED_LANGUAGE_KEY] = language
    }

    fun mapToAppException(error: Throwable): AppException = errorMapper.map(error)

    fun defaultLanguage(): String = Constants.languages.firstOrNull() ?: DEFAULT_LANGUAGE

    private companion object {
        const val SELECTED_LANGUAGE_KEY = "selected_language"
        const val DEFAULT_LANGUAGE = "Kotlin"
    }
}
