package com.caioluis.githubpopular.ui

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
import com.caioluis.githubpopular.model.UiGitHubRepository

@Composable
fun RepositoriesList(
    repositories: LazyPagingItems<UiGitHubRepository>,
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
            count = repositories.itemCount,
            key = { index ->
                val item = repositories.peek(index)
                item?.id ?: "placeholder_$index"
            },
        ) { index ->
            val repository = repositories[index]

            if (repository != null) {
                RepositoryItem(repository = repository)
            } else {
                RepositoryItemPlaceholder()
            }
        }

        when (repositories.loadState.append) {
            is LoadState.Loading -> {
                item(key = "append_loading") {
                    RepositoryItemPlaceholder()
                }
            }

            is LoadState.Error -> {
                item(key = "append_error") {
                    ErrorContent(
                        error = (repositories.loadState.append as LoadState.Error).error,
                        onRetry = { repositories.retry() },
                    )
                }
            }

            else -> Unit
        }
    }
}
