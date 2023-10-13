package com.caioluis.githubpopular.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.ui.PreviewsData.getPreviewData
import com.caioluis.githubpopular.ui.theme.GitHubPopularTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun RepositoriesList(
    modifier: Modifier = Modifier,
    data: List<UiGitHubRepository>,
    onEndReached: () -> Unit,
    isLoading: Boolean,
    error: Throwable? = null
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize()
    ) {
        items(data) { item ->
            ItemCard(item)
        }

        if (isLoading) {
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                    )
                }
            }
        }

        if (error != null) {
            item {
                ErrorComponent { onEndReached() }
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .filter { index ->
                index + 1 >= data.size - 5
            }
            .distinctUntilChanged()
            .collect {
                onEndReached()
            }
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoriesListPreview() {
    GitHubPopularTheme {
        RepositoriesList(
            data = getPreviewData(8),
            onEndReached = { },
            isLoading = false
        )
    }
}
