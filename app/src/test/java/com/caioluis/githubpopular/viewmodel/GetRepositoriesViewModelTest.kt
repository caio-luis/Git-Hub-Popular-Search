package com.caioluis.githubpopular.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.caioluis.githubpopular.MainDispatcherRule
import com.caioluis.githubpopular.domain.bridge.model.Response
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.mapper.Fixtures.domainGitHubRepository
import com.caioluis.githubpopular.mapper.Fixtures.uiRepository
import com.caioluis.githubpopular.model.UiGitHubRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
    fun `assert that call loadList returns response success`() = runTest {
        // given
        val uiRepos = listOf(uiRepository)
        val domainRepos = listOf(domainGitHubRepository)
        val emittedValues = mutableListOf<Response<List<UiGitHubRepository>>>()

        coEvery { getRepositoriesUseCase.loadRepositories(any()) } coAnswers {
            flowOf(domainRepos)
        }

        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.repositoriesFlow.collect {
                emittedValues.add(it)
            }
        }

        // when
        viewModel.loadList("")
        advanceUntilIdle()

        // then
        assertTrue(emittedValues.isNotEmpty())
        val lastValue = emittedValues.last()
        lastValue.handleResponse(
            onSuccess = { assertEquals(uiRepos, it) },
            onFailure = { assertNull(it) },
        )

        job.cancel()
    }

    @Test
    fun `should handle error when loading list of repositories`() = runTest {
        // given
        val expectedError = Exception("error")
        val emittedValues = mutableListOf<Response<List<UiGitHubRepository>>>()

        coEvery { getRepositoriesUseCase.loadRepositories(any()) } throws expectedError

        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.repositoriesFlow.collect {
                emittedValues.add(it)
            }
        }

        // when
        viewModel.loadList("")
        advanceUntilIdle()

        // then
        assertTrue(emittedValues.isNotEmpty())
        val lastValue = emittedValues.last()

        var failureException: Throwable? = null
        lastValue.handleResponse(
            onSuccess = { assertNull(it) },
            onFailure = { failureException = it },
        )
        assertEquals(expectedError, failureException)

        job.cancel()
    }

    @Test
    fun `should handle onLoading when loading`() = runTest {
        // given
        val emittedValues = mutableListOf<Response<List<UiGitHubRepository>>>()
        coEvery { getRepositoriesUseCase.loadRepositories(any()) } coAnswers {
            flowOf(listOf(domainGitHubRepository))
        }

        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.repositoriesFlow.collect {
                emittedValues.add(it)
            }
        }

        // when
        viewModel.loadList("test")
        advanceUntilIdle()

        // then
        val hasLoading = emittedValues.any { it is Response.Loading }
        assertTrue(hasLoading)

        job.cancel()
    }
}
