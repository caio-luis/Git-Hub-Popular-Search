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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.caioluis.githubpopular.githubpulls.viewmodel.GetPullRequestsViewModel
import com.caioluis.githubpopular.ui.ErrorContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRequestsScreen(
    pullUrl: String,
    repositoryId: Int,
    repositoryName: String,
    onBackClick: () -> Unit,
    getPullRequestsViewModel: GetPullRequestsViewModel = hiltViewModel(),
) {
    val pullRequests = getPullRequestsViewModel.pullRequests.collectAsLazyPagingItems()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(pullUrl, repositoryId) {
        getPullRequestsViewModel.loadList(pullUrl, repositoryId)
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
                    error = refreshState.error,
                    onRetry = { pullRequests.retry() },
                )
            } else {
                key(repositoryId) {
                    PullRequestsList(
                        pullRequests = pullRequests,
                    )
                }
            }
        }
    }
}
