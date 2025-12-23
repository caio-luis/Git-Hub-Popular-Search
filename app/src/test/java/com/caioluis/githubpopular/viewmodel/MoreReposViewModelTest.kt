package com.caioluis.githubpopular.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.caioluis.githubpopular.MainDispatcherRule
import com.caioluis.githubpopular.domain.bridge.usecase.GetMoreReposUseCase
import com.caioluis.githubpopular.mapper.Fixtures.domainGitHubRepository
import com.caioluis.githubpopular.mapper.Fixtures.uiRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MoreReposViewModelTest {
    private lateinit var viewModel: MoreReposViewModel
    private lateinit var getMoreRepositoriesUseCase: GetMoreReposUseCase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0

        getMoreRepositoriesUseCase = mockk(relaxed = true)
        viewModel = MoreReposViewModel(getMoreRepositoriesUseCase)
    }

    @Test
    fun `assert that call loadMore() returns response success`() {
        runTest {
            // given
            val uiRepos = listOf(uiRepository)
            val domainRepos = listOf(domainGitHubRepository)

            coEvery { getMoreRepositoriesUseCase.loadRepositories(any()) } coAnswers {
                flowOf(domainRepos)
            }

            // when
            viewModel.loadMore("")

            // then
            viewModel.observeMoreReposLiveData.observeForever { response ->
                response.handleResponse(
                    onSuccess = { assertEquals(uiRepos, it) },
                    onFailure = { assertNull(it) },
                )
            }
        }
    }

    @Test
    fun `should handle error when loading list of repositories`() =
        runTest {
            // given
            val expectedError = Exception("error")
            coEvery { getMoreRepositoriesUseCase.loadRepositories(any()) } throws (expectedError)

            // when
            viewModel.loadMore("")

            // then
            viewModel.observeMoreReposLiveData.observeForever { response ->
                response.handleResponse(
                    onSuccess = { assertNull(it) },
                    onFailure = { assertEquals(expectedError, it) },
                )
            }
        }

    @Test
    fun `should handle onLoading when loading`() =
        runTest {
            // given
            var loadCalled = false
            coEvery { getMoreRepositoriesUseCase.loadRepositories(any()) } coAnswers {
                flowOf(listOf(domainGitHubRepository))
            }

            // when
            viewModel.loadMore("test")

            // then
            viewModel.observeMoreReposLiveData.observeForever { response ->
                response.handleResponse(
                    onLoading = {
                        loadCalled = true
                    },
                )

                assertTrue(loadCalled)
            }
        }
}
