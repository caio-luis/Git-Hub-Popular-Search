package com.caioluis.githubpopular.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.caioluis.githubpopular.MainDispatcherRule
import com.caioluis.githubpopular.impl.usecases.GetRepositoriesUseCase
import com.caioluis.githubpopular.mapper.Fixtures.domainGitHubRepository
import com.caioluis.githubpopular.mapper.Fixtures.uiRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GitHubRepositoriesViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var getRepositoriesUseCase: GetRepositoriesUseCase
    private lateinit var viewModel: GitHubRepositoriesViewModel

    @Before
    fun setUp() {
        getRepositoriesUseCase = mockk(relaxed = true)
        viewModel = GitHubRepositoriesViewModel(getRepositoriesUseCase)
    }

    @Test
    fun `assert that call loadList returns response success`() {
        runTest {
            // given
            val uiRepos = listOf(uiRepository)
            val domainRepos = listOf(domainGitHubRepository)

            coEvery { getRepositoriesUseCase.loadRepositories(any()) } coAnswers {
                Result.success(domainRepos)
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
        coEvery { getRepositoriesUseCase.loadRepositories(any()) } coAnswers {
            Result.failure(expectedError)
        }

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
        var loadCalled: Boolean
        coEvery { getRepositoriesUseCase.loadRepositories(any()) } coAnswers {
            Result.success(listOf(domainGitHubRepository))
        }

        // when
        viewModel.loadList("test")

        // then
        viewModel.observeGitHubReposLiveData.observeForever { response ->
            response.handleResponse(
                onLoading = {
                    loadCalled = true
                    assertTrue(loadCalled)
                }
            )
        }
    }
}
