package com.caioluis.githubpopular.ui.githubrepos

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
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.ui.EmptyContent
import com.caioluis.githubpopular.ui.EndOfListContent
import com.caioluis.githubpopular.ui.ErrorContent

@Composable
fun RepositoriesList(
    repositories: LazyPagingItems<UiGitHubRepository>,
    onRepositoryClick: (UiGitHubRepository) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    if (repositories.loadState.refresh is LoadState.NotLoading && repositories.itemCount == 0) {
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
            count = repositories.itemCount,
            key = { index ->
                val item = repositories.peek(index)
                item?.id ?: "placeholder_$index"
            },
        ) { index ->
            val repository = repositories[index]

            if (repository != null) {
                RepositoryItem(
                    repository = repository,
                    onClick = onRepositoryClick,
                )
            } else {
                RepositoryItemPlaceholder()
            }
        }

        when (val appendState = repositories.loadState.append) {
            is LoadState.Loading -> {
                item(key = "append_loading") {
                    RepositoryItemPlaceholder()
                }
            }

            is LoadState.Error -> {
                item(key = "append_error") {
                    ErrorContent(
                        error = appendState.error,
                        onRetry = { repositories.retry() },
                    )
                }
            }

            is LoadState.NotLoading -> {
                if (appendState.endOfPaginationReached && repositories.itemCount > 0) {
                    item(key = "append_end") {
                        EndOfListContent(message = stringResource(id = R.string.no_more_items_to_display))
                    }
                }
            }
        }
    }
}
