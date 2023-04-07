package com.caioluis.githubpopular.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.caioluis.domain.base.Response
import com.caioluis.domain.base.Response.Loading
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.usecases.GetRepositoriesUseCase
import com.caioluis.githubpopular.MainDispatcherRule
import com.caioluis.githubpopular.mapper.Fixtures.domainGitHubRepository
import com.caioluis.githubpopular.mapper.Fixtures.uiRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.After
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
    private lateinit var channel: Channel<Response<List<DomainGitHubRepository>>>

    @Before
    fun setUp() {
        getRepositoriesUseCase = mockk(relaxed = true)
        channel = Channel(capacity = 1)
        coEvery { getRepositoriesUseCase.receiveChannel } returns channel

        viewModel = GitHubRepositoriesViewModel(getRepositoriesUseCase)
    }

    @After
    fun tearDown() {
        channel.close()
    }

    @Test
    fun `assert that call loadList returns response success`() {
        runBlocking {
            // given
            val uiRepos = listOf(uiRepository)
            val domainRepos = listOf(domainGitHubRepository)

            coEvery { getRepositoriesUseCase.invoke(any()) } coAnswers {
                channel.send(Response.Success(domainRepos))
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
    fun `should handle error when loading list of repositories`() = runBlocking {
        // given
        val expectedError = Exception("error")
        coEvery { getRepositoriesUseCase() } coAnswers {
            channel.send(Response.Failure(expectedError))
        }

        // when
        viewModel.loadList("")

        // then
        viewModel.observeGitHubReposLiveData.observeForever { response ->
            response.handleResponse(
                onSuccess = { assertNull(it) },
                onFailure = { assertEquals(expectedError, it) }
            )
        }
    }

    @Test
    fun `should handle onLoading when loading`() = runBlocking {
        // given
        coEvery { getRepositoriesUseCase() } coAnswers {
            channel.send(Loading)
        }

        var loadCalled = false

        // when
        viewModel.loadList("")

        // then
        viewModel.observeGitHubReposLiveData.observeForever { response ->
            response.handleResponse(
                onSuccess = { assertNull(it) },
                onFailure = { assertNull(it) },
                onLoading = { loadCalled = true }
            )

            assertTrue(loadCalled)
        }
    }
}
