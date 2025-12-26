package com.caioluis.githubpopular.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.caioluis.githubpopular.MainDispatcherRule
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.mapper.Fixtures.domainGitHubRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetRepositoriesViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = MainDispatcherRule(testDispatcher)

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
    fun `repositories - should return flow of PagingData`() = runTest(testDispatcher) {
        // given
        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } returns listOf(
            domainGitHubRepository,
        )

        // when
        viewModel.loadList("Kotlin")
        val pagingDataFlow = viewModel.repositories

        // then
        val pagingData = pagingDataFlow.first()
        assertNotNull(pagingData)
    }

    @Test
    fun `repositories - should emit new PagingData when language changes`() = runTest(testDispatcher) {
        // given
        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } returns listOf(
            domainGitHubRepository,
        )

        // when
        viewModel.loadList("Kotlin")
        val firstPagingData = viewModel.repositories.first()

        viewModel.loadList("Java")
        val secondPagingData = viewModel.repositories.first()

        // then
        assertNotNull(firstPagingData)
        assertNotNull(secondPagingData)
    }
}
