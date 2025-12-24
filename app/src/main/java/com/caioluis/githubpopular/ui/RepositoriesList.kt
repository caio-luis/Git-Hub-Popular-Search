package com.caioluis.githubpopular.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.model.UiRepositoryOwner

@Composable
fun RepositoriesList(
    repositories: List<UiGitHubRepository>,
    isLoadingMore: Boolean,
    loadMoreError: Throwable?,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    val isScrollToEnd by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(isScrollToEnd) {
        if (isScrollToEnd && !isLoadingMore && loadMoreError == null && repositories.isNotEmpty()) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(repositories) { repository ->
            RepositoryItem(repository = repository)
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        if (loadMoreError != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    OutlinedButton(onClick = onLoadMore) {
                        Text(text = "Retry")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoriesListPreview() {
    val dummyRepositories = List(10) { index ->
        UiGitHubRepository(
            name = "Awesome Repo $index",
            description = "This is a very awesome repository with a long description to test how it looks in the list item.",
            stargazersCount = 1234 + index,
            forksCount = 567 + index,
            owner = UiRepositoryOwner(login = "dev_guru_$index", avatarUrl = ""),
        )
    }

    RepositoriesList(
        repositories = dummyRepositories,
        isLoadingMore = false,
        loadMoreError = null,
        onLoadMore = {},
    )
}
