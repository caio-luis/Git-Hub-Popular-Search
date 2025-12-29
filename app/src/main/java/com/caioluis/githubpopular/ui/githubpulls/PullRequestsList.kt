package com.caioluis.githubpopular.ui.githubpulls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.caioluis.githubpopular.model.UiGitHubPullRequest
import com.caioluis.githubpopular.ui.ErrorContent
import com.caioluis.githubpopular.ui.githubrepos.RepositoryItemPlaceholder

@Composable
fun PullRequestsList(
    pullRequests: LazyPagingItems<UiGitHubPullRequest>,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            count = pullRequests.itemCount,
            key = { index ->
                val item = pullRequests.peek(index)
                item?.id ?: "placeholder_$index"
            },
        ) { index ->
            val pullRequest = pullRequests[index]

            if (pullRequest != null) {
                PullRequestItem(pullRequest = pullRequest)
            } else {
                RepositoryItemPlaceholder()
            }
        }

        when (pullRequests.loadState.append) {
            is LoadState.Loading -> {
                item(key = "append_loading") {
                    RepositoryItemPlaceholder()
                }
            }

            is LoadState.Error -> {
                item(key = "append_error") {
                    ErrorContent(
                        error = (pullRequests.loadState.append as LoadState.Error).error,
                        onRetry = { pullRequests.retry() },
                    )
                }
            }

            else -> Unit
        }
    }
}
