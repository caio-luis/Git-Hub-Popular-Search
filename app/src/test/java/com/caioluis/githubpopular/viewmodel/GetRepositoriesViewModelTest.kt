package com.caioluis.githubpopular.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.caioluis.githubpopular.MainDispatcherRule
import com.caioluis.githubpopular.domain.pub.usecase.GetRepositoriesUseCase
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
class GetRepositoriesViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var getRepositoriesUseCase: GetRepositoriesUseCase
    private lateinit var viewModel: GetRepositoriesViewModel

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0

        getRepositoriesUseCase = mockk(relaxed = true)
        viewModel = GetRepositoriesViewModel(getRepositoriesUseCase)
    }

    @Test
    fun `assert that call loadList returns response success`() {
        runTest {
            // given
            val uiRepos = listOf(uiRepository)
            val domainRepos = listOf(domainGitHubRepository)

            coEvery { getRepositoriesUseCase.loadRepositories(any()) } coAnswers {
                flowOf(domainRepos)
            }

            // when
            viewModel.loadList("")

            // then
            viewModel.observeGitHubReposLiveData.observeForever { response ->
                response.handleResponse(
                    onSuccess = { assertEquals(uiRepos, it) },
                    onFailure = { assertNull(it) },
                )
            }
        }
    }

    @Test
    fun `should handle error when loading list of repositories`() = runTest {
        // given
        val expectedError = Exception("error")
        coEvery { getRepositoriesUseCase.loadRepositories(any()) } throws expectedError

        // when
        viewModel.loadList("")

        // then
        viewModel.observeGitHubReposLiveData.observeForever { response ->
            response.handleResponse(
                onSuccess = { assertNull(it) },
                onFailure = { assertEquals(expectedError, it) },
            )
        }
    }

    @Test
    fun `should handle onLoading when loading`() = runTest {
        // given
        var loadCalled = false
        coEvery { getRepositoriesUseCase.loadRepositories(any()) } coAnswers {
            flowOf(listOf(domainGitHubRepository))
        }

        // when
        viewModel.loadList("test")

        // then
        viewModel.observeGitHubReposLiveData.observeForever { response ->
            response.handleResponse(
                onLoading = {
                    loadCalled = true
                }
            )

            assertTrue(loadCalled)
        }
    }
}
