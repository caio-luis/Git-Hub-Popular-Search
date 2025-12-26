package com.caioluis.githubpopular.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.caioluis.githubpopular.Constants
import com.caioluis.githubpopular.viewmodel.GetRepositoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    getRepositoriesViewModel: GetRepositoriesViewModel = hiltViewModel(),
) {
    val uiState by getRepositoriesViewModel.uiState.collectAsStateWithLifecycle()

    var selectedLanguage by remember { mutableStateOf(Constants.languages.first()) }
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(selectedLanguage) {
        getRepositoriesViewModel.loadList(selectedLanguage)
    }

    Scaffold(
        topBar = {
            LanguageSelector(
                selectedLanguage = selectedLanguage,
                languages = Constants.languages,
                onLanguageSelected = { selectedLanguage = it },
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            state = pullToRefreshState,
            onRefresh = { getRepositoriesViewModel.loadList(selectedLanguage) },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            when {
                uiState.error != null -> ErrorContent(
                    error = uiState.error,
                    onRetry = { getRepositoriesViewModel.loadList(selectedLanguage) },
                )

                else -> RepositoriesList(
                    repositories = uiState.repositories,
                    isLoadingMore = uiState.isLoadingMore,
                    loadMoreError = uiState.loadMoreError,
                    onLoadMore = { getRepositoriesViewModel.loadMore(selectedLanguage) },
                )
            }
        }
    }
}
