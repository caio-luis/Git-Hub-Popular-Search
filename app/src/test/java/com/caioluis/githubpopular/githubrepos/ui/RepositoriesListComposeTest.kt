package com.caioluis.githubpopular.githubrepos.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.caioluis.githubpopular.core.common.exception.AppException
import com.caioluis.githubpopular.fixtures.sampleRepositories
import com.caioluis.githubpopular.githubrepos.model.UiGitHubRepo
import com.caioluis.githubpopular.theme.GitHubPopularTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "pt-rBR")
class RepositoriesListComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `should display repository items when data is loaded`() {
        val pagingData = PagingData.from(
            data = sampleRepositories,
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
                RepositoriesList(
                    repositories = lazyPagingItems,
                    onRepositoryClick = {},
                    mapError = { AppException.UnknownException(it) },
                )
            }
        }

        composeTestRule.onNodeWithText("kotlin").assertIsDisplayed()
        composeTestRule.onNodeWithText("The Kotlin Programming Language").assertIsDisplayed()
        composeTestRule.onNodeWithText("JetBrains").assertIsDisplayed()

        composeTestRule.onNodeWithText("okhttp").assertIsDisplayed()
        composeTestRule.onNodeWithText("Square's meticulous HTTP client for the JVM")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("square").assertIsDisplayed()
    }

    @Test
    fun `should display empty content when list is empty and not loading`() {
        val pagingData = PagingData.from(
            data = emptyList<UiGitHubRepo>(),
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
                RepositoriesList(
                    repositories = lazyPagingItems,
                    onRepositoryClick = {},
                    mapError = { AppException.UnknownException(it) },
                )
            }
        }

        composeTestRule.onNodeWithText("Não há itens para exibir").assertIsDisplayed()
    }

    @Test
    fun `should display end of list message when pagination is exhausted`() {
        val pagingData = PagingData.from(
            data = sampleRepositories,
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
                RepositoriesList(
                    repositories = lazyPagingItems,
                    onRepositoryClick = {},
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
            data = sampleRepositories,
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
                RepositoriesList(
                    repositories = lazyPagingItems,
                    onRepositoryClick = {},
                    mapError = { AppException.ServerException(it) },
                )
            }
        }

        composeTestRule.onNodeWithText("Servidor indisponível. Tente mais tarde.")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Tentar novamente").assertIsDisplayed()
    }
}
