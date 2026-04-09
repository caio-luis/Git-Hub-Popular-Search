package com.caioluis.githubpopular.viewmodel

import androidx.paging.PagingData
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.domain.bridge.usecase.GetPullRequestsUseCase
import com.caioluis.githubpopular.githubpulls.mapper.PullRequestUiMapper
import com.caioluis.githubpopular.githubpulls.viewmodel.GetPullRequestsViewModel
import com.caioluis.githubpopular.mapper.Fixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPullRequestsViewModelTest {

    private val getPullRequestsUseCase: GetPullRequestsUseCase = mockk()
    private val pullRequestUiMapper: PullRequestUiMapper = mockk()
    private val errorMapper: ErrorMapper = mockk()

    private lateinit var viewModel: GetPullRequestsViewModel

    private val unconfinedDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(unconfinedDispatcher)
        viewModel = GetPullRequestsViewModel(getPullRequestsUseCase, pullRequestUiMapper, errorMapper)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadListShouldNotFetchAgainIfRepositoryRequestIsTheSame() = runTest(unconfinedDispatcher) {
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
    fun loadListWithDifferentRepositoriesShouldFetchNewData() = runTest(unconfinedDispatcher) {
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
}
