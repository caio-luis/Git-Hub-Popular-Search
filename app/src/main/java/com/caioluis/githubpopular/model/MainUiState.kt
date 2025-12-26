package com.caioluis.githubpopular.model

data class MainUiState(
    val repositories: List<UiGitHubRepository> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isLoadingMore: Boolean = false,
    val loadMoreError: Throwable? = null,
)
