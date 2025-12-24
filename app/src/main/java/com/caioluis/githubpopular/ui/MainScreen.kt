package com.caioluis.githubpopular.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.caioluis.githubpopular.Constants
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.model.UiRepositoryOwner
import com.caioluis.githubpopular.viewmodel.GetRepositoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    getRepositoriesViewModel: GetRepositoriesViewModel = hiltViewModel(),
) {
    val repositoriesResponse by getRepositoriesViewModel.repositoriesFlow.collectAsStateWithLifecycle()
    val isLoadingMore by getRepositoriesViewModel.isLoadingMore.collectAsStateWithLifecycle()
    val loadMoreError by getRepositoriesViewModel.loadMoreError.collectAsStateWithLifecycle()

    var selectedLanguage by remember { mutableStateOf(Constants.languages.first()) }
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedLanguage) {
        getRepositoriesViewModel.loadList(selectedLanguage)
    }

    val repositories = remember(repositoriesResponse) {
        var list: List<UiGitHubRepository> = emptyList()
        repositoriesResponse.handleResponse(
            onSuccess = { list = it },
        )
        list
    }

    var isRefreshing = false
    var mainError: Throwable? = null

    repositoriesResponse.handleResponse(
        onLoading = { isRefreshing = true },
        onFailure = { mainError = it },
    )

    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = !isExpanded },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextField(
                        value = selectedLanguage,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Language") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth(),
                    )
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                    ) {
                        Constants.languages.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(text = language) },
                                onClick = {
                                    selectedLanguage = language
                                    isExpanded = false
                                },
                            )
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            state = pullToRefreshState,
            onRefresh = { getRepositoriesViewModel.loadList(selectedLanguage) },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            if (mainError != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Error: ${mainError?.message ?: "Unknown error"}")
                        Button(onClick = { getRepositoriesViewModel.loadList(selectedLanguage) }) {
                            Text(text = "Retry")
                        }
                    }
                }
            } else {
                RepositoriesList(
                    repositories = repositories,
                    isLoadingMore = isLoadingMore,
                    loadMoreError = loadMoreError,
                    onLoadMore = { getRepositoriesViewModel.loadMore(selectedLanguage) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    RepositoryItem(
        repository = UiGitHubRepository(
            name = "Example of title",
            description = "This is a example of a kotlin repository description. It'll appear like this to the user!",
            stargazersCount = 1234565,
            forksCount = 1234565,
            owner = UiRepositoryOwner(login = "User Name"),
        ),
    )
}
