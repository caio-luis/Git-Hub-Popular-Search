package com.caioluis.githubpopular.githubpulls.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.core.common.exception.AppException
import com.caioluis.githubpopular.githubpulls.model.UiGitHubPullRequest
import com.caioluis.githubpopular.githubrepos.ui.RepositoryItemPlaceholder
import com.caioluis.githubpopular.ui.EmptyContent
import com.caioluis.githubpopular.ui.EndOfListContent
import com.caioluis.githubpopular.ui.ErrorContent

private const val KEY_PLACEHOLDER_PREFIX = "placeholder_"
private const val KEY_APPEND_LOADING = "append_loading"
private const val KEY_APPEND_ERROR = "append_error"
private const val KEY_APPEND_END = "append_end"

@Composable
fun PullRequestsList(
    pullRequests: LazyPagingItems<UiGitHubPullRequest>,
    onPullRequestClick: (UiGitHubPullRequest) -> Unit,
    mapError: (Throwable) -> AppException,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    if (pullRequests.loadState.refresh is LoadState.NotLoading && pullRequests.itemCount == 0) {
        EmptyContent(message = stringResource(id = R.string.no_items_to_display))
        return
    }

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
                item?.id ?: "$KEY_PLACEHOLDER_PREFIX$index"
            },
        ) { index ->
            val pullRequest = pullRequests[index]

            if (pullRequest != null) {
                PullRequestItem(
                    pullRequest = pullRequest,
                    onClick = onPullRequestClick,
                )
            } else {
                RepositoryItemPlaceholder()
            }
        }

        when (val appendState = pullRequests.loadState.append) {
            is LoadState.Loading -> {
                item(key = KEY_APPEND_LOADING) {
                    RepositoryItemPlaceholder()
                }
            }

            is LoadState.Error -> {
                item(key = KEY_APPEND_ERROR) {
                    ErrorContent(
                        error = mapError(appendState.error),
                        onRetry = { pullRequests.retry() },
                    )
                }
            }

            is LoadState.NotLoading -> {
                if (appendState.endOfPaginationReached && pullRequests.itemCount > 0) {
                    item(key = KEY_APPEND_END) {
                        EndOfListContent(message = stringResource(id = R.string.no_more_items_to_display))
                    }
                }
            }
        }
    }
}
