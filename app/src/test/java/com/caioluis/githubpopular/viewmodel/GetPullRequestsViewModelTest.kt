package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.caioluis.githubpopular.core.common.exception.AppException
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.domain.bridge.usecase.GetPullRequestsUseCase
import com.caioluis.githubpopular.githubpulls.mapper.PullRequestUiMapper
import com.caioluis.githubpopular.githubpulls.viewmodel.GetPullRequestsViewModel
import com.caioluis.githubpopular.mapper.Fixtures
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
                    "pullUrl" to "https://api.github.com/repos/user/repo/pulls",
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
    fun loadListShouldNotFetchAgainIfRepositoryRequestIsTheSame() = runTest(mainDispatcherRule.dispatcher) {
        val pullUrl = "https://api.github.com/repos/user/repo/pulls"
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
    fun loadListWithDifferentRepositoriesShouldFetchNewData() = runTest(mainDispatcherRule.dispatcher) {
        val pullUrl = "https://api.github.com/repos/user/repo/pulls"
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
    fun `repository metadata should be initialized from saved state and repository id should update`() {
        assertEquals("repo", viewModel.repositoryName.value)
        assertEquals(Fixtures.REPOSITORY_ID, viewModel.currentRepositoryId)

        viewModel.loadList(
            pullUrl = "https://api.github.com/repos/user/new-repo/pulls",
            repositoryId = 999,
            repositoryName = "new-repo",
        )

        assertEquals(999, viewModel.currentRepositoryId)
        assertEquals("new-repo", viewModel.repositoryName.value)
    }

    @Test
    fun `mapToAppException should return same instance when throwable is already AppException`() {
        val appException = AppException.TimeoutException(Throwable("timeout"))

        val result = viewModel.mapToAppException(appException)

        assertEquals(appException, result)
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
