package com.caioluis.githubpopular.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.caioluis.domain.base.Response
import com.caioluis.domain.base.Response.Failure
import com.caioluis.domain.base.Response.Loading
import com.caioluis.domain.base.Response.Success
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.usecases.GetMoreReposUseCase
import com.caioluis.githubpopular.MainDispatcherRule
import com.caioluis.githubpopular.mapper.Fixtures.domainGitHubRepository
import com.caioluis.githubpopular.mapper.Fixtures.uiRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MoreReposViewModelTest {
    private lateinit var viewModel: MoreReposViewModel
    private lateinit var getMoreRepositoriesUseCase: GetMoreReposUseCase
    private lateinit var channel: Channel<Response<List<DomainGitHubRepository>>>

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    @Before
    fun setUp() {
        getMoreRepositoriesUseCase = mockk(relaxed = true)
        channel = Channel(capacity = 1)
        coEvery { getMoreRepositoriesUseCase.receiveChannel } returns channel

        viewModel = MoreReposViewModel(getMoreRepositoriesUseCase)
    }

    @After
    fun tearDown() {
        channel.close()
    }

    @Test
    fun `assert that call loadMore() returns response success`() {
        runBlocking {
            // given
            val uiRepos = listOf(uiRepository)
            val domainRepos = listOf(domainGitHubRepository)

            coEvery { getMoreRepositoriesUseCase.invoke(any()) } coAnswers {
                channel.send(Success(domainRepos))
            }

            // when
            viewModel.loadMore()

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
    fun `should handle error when loading list of repositories`() = runBlocking {
        // given
        val expectedError = Exception("error")
        coEvery { getMoreRepositoriesUseCase() } coAnswers {
            channel.send(Failure(expectedError))
        }

        // when
        viewModel.loadMore()

        // then
        viewModel.observeMoreReposLiveData.observeForever { response ->
            response.handleResponse(
                onSuccess = { assertNull(it) },
                onFailure = { assertEquals(expectedError, it) }
            )
        }
    }

    @Test
    fun `should handle onLoading when loading`() = runBlocking {
        // given
        coEvery { getMoreRepositoriesUseCase() } coAnswers {
            channel.send(Loading)
        }

        var loadCalled = false

        // when
        viewModel.loadMore()

        // then
        viewModel.observeMoreReposLiveData.observeForever { response ->
            response.handleResponse(
                onSuccess = { assertNull(it) },
                onFailure = { assertNull(it) },
                onLoading = { loadCalled = true }
            )

            Assert.assertTrue(loadCalled)
        }
    }
}
