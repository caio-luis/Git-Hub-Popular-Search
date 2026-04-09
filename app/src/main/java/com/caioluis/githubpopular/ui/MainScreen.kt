package com.caioluis.githubpopular.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.caioluis.githubpopular.githubrepos.model.UiGitHubRepo
import com.caioluis.githubpopular.githubrepos.ui.LanguageSelector
import com.caioluis.githubpopular.githubrepos.ui.RepositoriesList
import com.caioluis.githubpopular.githubrepos.viewmodel.GetRepositoriesViewModel

@Composable
fun MainScreen(
    onRepositoryClick: (UiGitHubRepo) -> Unit,
    getRepositoriesViewModel: GetRepositoriesViewModel = hiltViewModel(),
) {
    val repositories = getRepositoriesViewModel.repositories.collectAsLazyPagingItems()
    val viewModelSelectedLanguage by getRepositoriesViewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedLanguage = viewModelSelectedLanguage ?: getRepositoriesViewModel.defaultLanguage()

    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(viewModelSelectedLanguage) {
        if (viewModelSelectedLanguage == null) {
            getRepositoriesViewModel.loadList(getRepositoriesViewModel.defaultLanguage())
        }
    }

    val isRefreshing = repositories.loadState.refresh is LoadState.Loading

    Scaffold(
        topBar = {
            LanguageSelector(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = getRepositoriesViewModel::loadList,
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
            var stickyRefreshError by remember(selectedLanguage) { mutableStateOf<Throwable?>(null) }

            LaunchedEffect(refreshState, repositories.itemCount) {
                stickyRefreshError = resolveStickyRefreshError(
                    currentError = stickyRefreshError,
                    refreshState = refreshState,
                    itemCount = repositories.itemCount,
                )
            }

            val errorToShow = (refreshState as? LoadState.Error)?.error ?: stickyRefreshError

            if (repositories.itemCount == 0 && errorToShow != null) {
                ErrorContent(
                    error = getRepositoriesViewModel.mapToAppException(errorToShow),
                    onRetry = { repositories.retry() },
                )
            } else {
                key(selectedLanguage) {
                    RepositoriesList(
                        repositories = repositories,
                        onRepositoryClick = onRepositoryClick,
                        mapError = getRepositoriesViewModel::mapToAppException,
                    )
                }
            }
        }
    }
}

internal fun resolveStickyRefreshError(
    currentError: Throwable?,
    refreshState: LoadState,
    itemCount: Int,
): Throwable? {
    if (itemCount > 0) return null

    return when (refreshState) {
        is LoadState.Error -> refreshState.error
        is LoadState.NotLoading -> null
        is LoadState.Loading -> currentError
    }
}
