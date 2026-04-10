package com.caioluis.githubpopular.githubpulls.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.caioluis.githubpopular.core.common.exception.AppException
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.domain.bridge.usecase.GetPullRequestsUseCase
import com.caioluis.githubpopular.fixtures.Fixtures
import com.caioluis.githubpopular.githubpulls.mapper.PullRequestUiMapper
import com.caioluis.githubpopular.rules.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPullRequestsViewModelTest {

    private val getPullRequestsUseCase: GetPullRequestsUseCase = mockk()
    private val pullRequestUiMapper: PullRequestUiMapper = mockk()
    private val errorMapper: ErrorMapper = mockk()

    private lateinit var viewModel: GetPullRequestsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = GetPullRequestsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "pullUrl" to "https://api.test.com/repos/user/repo/pulls",
                    "repositoryId" to Fixtures.REPOSITORY_ID,
                    "repositoryName" to "repo",
                ),
            ),
            getPullRequestsUseCase = getPullRequestsUseCase,
            pullRequestUiMapper = pullRequestUiMapper,
            errorMapper = errorMapper,
        )
    }

    @Test
    fun `load list should not fetch again if repository request is the same`() = runTest(mainDispatcherRule.dispatcher) {
        val pullUrl = "https://api.test.com/repos/user/repo/pulls"
        val repositoryId = Fixtures.REPOSITORY_ID
        every {
            getPullRequestsUseCase.loadPullRequests(
                pullUrl = any(),
                repositoryId = any(),
            )
        } returns flowOf(PagingData.empty())

        val job = backgroundScope.launch {
            viewModel.pullRequests.collect {}
        }

        viewModel.loadList(pullUrl, repositoryId)
        viewModel.loadList(pullUrl, repositoryId)

        verify(exactly = 1) {
            getPullRequestsUseCase.loadPullRequests(
                pullUrl = pullUrl,
                repositoryId = repositoryId,
            )
        }

        job.cancel()
    }

    @Test
    fun `load list with different repositories should fetch new data`() = runTest(mainDispatcherRule.dispatcher) {
        val pullUrl = "https://api.test.com/repos/user/repo/pulls"
        val initialRepositoryId = 1
        val newRepositoryId = 2
        every {
            getPullRequestsUseCase.loadPullRequests(
                pullUrl = any(),
                repositoryId = any(),
            )
        } returns flowOf(PagingData.empty())

        val job = backgroundScope.launch {
            viewModel.pullRequests.collect {}
        }

        viewModel.loadList(pullUrl, initialRepositoryId)
        viewModel.loadList(pullUrl, newRepositoryId)

        verify(exactly = 1) {
            getPullRequestsUseCase.loadPullRequests(
                pullUrl = pullUrl,
                repositoryId = initialRepositoryId,
            )
        }
        verify(exactly = 1) {
            getPullRequestsUseCase.loadPullRequests(
                pullUrl = pullUrl,
                repositoryId = newRepositoryId,
            )
        }

        job.cancel()
    }

    @Test
    fun `load list should not emit mixed request state when url and id change together`() = runTest(mainDispatcherRule.dispatcher) {
        val initialPullUrl = "https://api.test.com/repos/user/repo/pulls"
        val initialRepositoryId = Fixtures.REPOSITORY_ID
        val newPullUrl = "https://api.test.com/repos/user/new-repo/pulls"
        val newRepositoryId = 999

        every {
            getPullRequestsUseCase.loadPullRequests(
                pullUrl = any(),
                repositoryId = any(),
            )
        } returns flowOf(PagingData.empty())

        val job = backgroundScope.launch {
            viewModel.pullRequests.collect {}
        }

        viewModel.loadList(newPullUrl, newRepositoryId)

        verify(exactly = 1) {
            getPullRequestsUseCase.loadPullRequests(
                pullUrl = initialPullUrl,
                repositoryId = initialRepositoryId,
            )
        }
        verify(exactly = 1) {
            getPullRequestsUseCase.loadPullRequests(
                pullUrl = newPullUrl,
                repositoryId = newRepositoryId,
            )
        }
        verify(exactly = 0) {
            getPullRequestsUseCase.loadPullRequests(
                pullUrl = newPullUrl,
                repositoryId = initialRepositoryId,
            )
        }
        verify(exactly = 0) {
            getPullRequestsUseCase.loadPullRequests(
                pullUrl = initialPullUrl,
                repositoryId = newRepositoryId,
            )
        }

        job.cancel()
    }

    @Test
    fun `repository metadata should be initialized from saved state and repository id should update`() {
        assertEquals("repo", viewModel.repositoryName.value)
        assertEquals(Fixtures.REPOSITORY_ID, viewModel.currentRepositoryId.value)

        viewModel.loadList(
            pullUrl = "https://api.test.com/repos/user/new-repo/pulls",
            repositoryId = 999,
            repositoryName = "new-repo",
        )

        assertEquals(999, viewModel.currentRepositoryId.value)
        assertEquals("new-repo", viewModel.repositoryName.value)
    }

    @Test
    fun `mapToAppException should return same instance when throwable is already AppException`() {
        val appException = AppException.TimeoutException(Throwable("timeout"))
        every { errorMapper.map(appException) } returns appException

        val result = viewModel.mapToAppException(appException)

        assertEquals(appException, result)
        verify(exactly = 1) { errorMapper.map(appException) }
    }

    @Test
    fun `mapToAppException should delegate to mapper for non app exceptions`() {
        val throwable = IllegalArgumentException("invalid")
        val mapped = AppException.UnknownException(throwable)
        every { errorMapper.map(throwable) } returns mapped

        val result = viewModel.mapToAppException(throwable)

        assertEquals(mapped, result)
        verify(exactly = 1) { errorMapper.map(throwable) }
    }
}
