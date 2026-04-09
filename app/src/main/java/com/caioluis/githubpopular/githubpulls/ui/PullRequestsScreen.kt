package com.caioluis.githubpopular.githubpulls.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.caioluis.githubpopular.core.common.extensions.openBrowserIntent
import com.caioluis.githubpopular.githubpulls.model.UiGitHubPullRequest
import com.caioluis.githubpopular.githubpulls.viewmodel.GetPullRequestsViewModel
import com.caioluis.githubpopular.ui.ErrorContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRequestsScreen(
    onBackClick: () -> Unit,
    getPullRequestsViewModel: GetPullRequestsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val pullRequests = getPullRequestsViewModel.pullRequests.collectAsLazyPagingItems()
    val repositoryName by getPullRequestsViewModel.repositoryName.collectAsStateWithLifecycle()
    val currentRepositoryId by getPullRequestsViewModel.currentRepositoryId.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()
    val onPullRequestClick = remember(context) {
        { pullRequest: UiGitHubPullRequest ->
            context.openBrowserIntent(pullRequest.htmlUrl)
        }
    }

    val isRefreshing = pullRequests.loadState.refresh is LoadState.Loading

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = repositoryName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            state = pullToRefreshState,
            onRefresh = { pullRequests.refresh() },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            val refreshState = pullRequests.loadState.refresh

            if (refreshState is LoadState.Error && pullRequests.itemCount == 0) {
                ErrorContent(
                    error = getPullRequestsViewModel.mapToAppException(refreshState.error),
                    onRetry = { pullRequests.retry() },
                )
            } else {
                key(currentRepositoryId) {
                    PullRequestsList(
                        pullRequests = pullRequests,
                        onPullRequestClick = onPullRequestClick,
                        mapError = getPullRequestsViewModel::mapToAppException,
                    )
                }
            }
        }
    }
}
