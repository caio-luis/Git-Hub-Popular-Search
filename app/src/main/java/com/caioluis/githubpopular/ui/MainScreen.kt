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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.caioluis.githubpopular.Constants
import com.caioluis.githubpopular.viewmodel.GetRepositoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    getRepositoriesViewModel: GetRepositoriesViewModel = hiltViewModel(),
) {
    val repositories = getRepositoriesViewModel.repositories.collectAsLazyPagingItems()

    var selectedLanguage by remember {
        mutableStateOf(Constants.languages.firstOrNull() ?: "Kotlin")
    }

    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(selectedLanguage) {
        getRepositoriesViewModel.loadList(selectedLanguage)
    }

    val isRefreshing = repositories.loadState.refresh is LoadState.Loading

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
            isRefreshing = isRefreshing,
            state = pullToRefreshState,
            onRefresh = { repositories.refresh() },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            val refreshState = repositories.loadState.refresh

            if (refreshState is LoadState.Error) {
                ErrorContent(
                    error = refreshState.error,
                    onRetry = { repositories.retry() },
                )
            } else {
                RepositoriesList(
                    repositories = repositories,
                )
            }
        }
    }
}
