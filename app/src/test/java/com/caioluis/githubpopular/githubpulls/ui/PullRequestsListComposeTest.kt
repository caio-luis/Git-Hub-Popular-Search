package com.caioluis.githubpopular.githubpulls.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.caioluis.githubpopular.core.common.exception.AppException
import com.caioluis.githubpopular.fixtures.samplePullRequests
import com.caioluis.githubpopular.githubpulls.model.UiGitHubPullRequest
import com.caioluis.githubpopular.theme.GitHubPopularTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "pt-rBR")
class PullRequestsListComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `should display pull request items when data is loaded`() {
        val pagingData = PagingData.from(
            data = samplePullRequests,
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(endOfPaginationReached = false),
                prepend = LoadState.NotLoading(endOfPaginationReached = true),
                append = LoadState.NotLoading(endOfPaginationReached = false),
            ),
        )
        val flow = MutableStateFlow(pagingData)

        composeTestRule.setContent {
            GitHubPopularTheme(dynamicColor = false) {
                val lazyPagingItems = flow.collectAsLazyPagingItems()
                PullRequestsList(
                    pullRequests = lazyPagingItems,
                    onPullRequestClick = {},
                    mapError = { AppException.UnknownException(it) },
                )
            }
        }

        composeTestRule.onNodeWithText("Fix memory leak in coroutine scope").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "This PR fixes a memory leak caused by not cancelling the coroutine scope on destroy.",
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText("JetBrains").assertIsDisplayed()

        composeTestRule.onNodeWithText("Add support for Kotlin 2.0").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "Adds full support for Kotlin 2.0 features including new compiler plugins.",
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText("square").assertIsDisplayed()
    }

    @Test
    fun `should display empty content when list is empty and not loading`() {
        val pagingData = PagingData.from(
            data = emptyList<UiGitHubPullRequest>(),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(endOfPaginationReached = true),
                prepend = LoadState.NotLoading(endOfPaginationReached = true),
                append = LoadState.NotLoading(endOfPaginationReached = true),
            ),
        )
        val flow = MutableStateFlow(pagingData)

        composeTestRule.setContent {
            GitHubPopularTheme(dynamicColor = false) {
                val lazyPagingItems = flow.collectAsLazyPagingItems()
                PullRequestsList(
                    pullRequests = lazyPagingItems,
                    onPullRequestClick = {},
                    mapError = { AppException.UnknownException(it) },
                )
            }
        }

        composeTestRule.onNodeWithText("Não há itens para exibir").assertIsDisplayed()
    }

    @Test
    fun `should display end of list message when pagination is exhausted`() {
        val pagingData = PagingData.from(
            data = samplePullRequests,
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(endOfPaginationReached = false),
                prepend = LoadState.NotLoading(endOfPaginationReached = true),
                append = LoadState.NotLoading(endOfPaginationReached = true),
            ),
        )
        val flow = MutableStateFlow(pagingData)

        composeTestRule.setContent {
            GitHubPopularTheme(dynamicColor = false) {
                val lazyPagingItems = flow.collectAsLazyPagingItems()
                PullRequestsList(
                    pullRequests = lazyPagingItems,
                    onPullRequestClick = {},
                    mapError = { AppException.UnknownException(it) },
                )
            }
        }

        composeTestRule.onNodeWithText("Não há mais itens para exibir").assertIsDisplayed()
    }

    @Test
    fun `should display error content when append fails`() {
        val error = Exception("Server error")
        val pagingData = PagingData.from(
            data = samplePullRequests,
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(endOfPaginationReached = false),
                prepend = LoadState.NotLoading(endOfPaginationReached = true),
                append = LoadState.Error(error),
            ),
        )
        val flow = MutableStateFlow(pagingData)

        composeTestRule.setContent {
            GitHubPopularTheme(dynamicColor = false) {
                val lazyPagingItems = flow.collectAsLazyPagingItems()
                PullRequestsList(
                    pullRequests = lazyPagingItems,
                    onPullRequestClick = {},
                    mapError = { AppException.ServerException(it) },
                )
            }
        }

        composeTestRule.onNodeWithText("Servidor indisponível. Tente mais tarde.")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Tentar novamente").assertIsDisplayed()
    }
}
