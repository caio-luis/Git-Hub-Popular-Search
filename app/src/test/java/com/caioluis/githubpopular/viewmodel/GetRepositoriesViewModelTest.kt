package com.caioluis.githubpopular.viewmodel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.caioluis.githubpopular.MainDispatcherRule
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.mapper.Fixtures.domainGitHubRepository
import com.caioluis.githubpopular.mapper.Fixtures.uiRepository
import com.caioluis.githubpopular.model.MainUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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
    fun `loadList - should update state to success when use case returns data`() = runTest(testDispatcher) {
        // given
        val domainRepos = listOf(domainGitHubRepository)
        val expectedUiRepos = listOf(uiRepository)

        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } returns domainRepos

        val states = mutableListOf<MainUiState>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testDispatcher.scheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // when
        viewModel.loadList("Kotlin")
        advanceUntilIdle()

        // then
        assertTrue(states.isNotEmpty())
        val finalState = states.last()
        assertFalse(finalState.isLoading)
        assertEquals(expectedUiRepos, finalState.repositories)
        assertNull(finalState.error)

        coVerify { getRepositoriesUseCase.loadRepositories(page = 1, language = "Kotlin") }

        job.cancel()
    }

    @Test
    fun `loadList - should update state to error when use case throws exception`() = runTest(testDispatcher) {
        // given
        val expectedError = RuntimeException("Network Error")

        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } throws expectedError

        val states = mutableListOf<MainUiState>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testDispatcher.scheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // when
        viewModel.loadList("Kotlin")
        advanceUntilIdle()

        // then
        val finalState = states.last()
        assertFalse(finalState.isLoading)
        assertTrue(finalState.repositories.isEmpty())
        assertEquals(expectedError, finalState.error)

        job.cancel()
    }

    @Test
    fun `loadMore - should append items when use case returns data`() = runTest(testDispatcher) {
        // given
        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } returns listOf(domainGitHubRepository)

        val states = mutableListOf<MainUiState>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testDispatcher.scheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // Initial load
        viewModel.loadList("Kotlin")
        advanceUntilIdle()

        // Prepare load more
        val newDomainRepos = listOf(domainGitHubRepository.copy(id = 456))
        val newUiRepos = listOf(uiRepository.copy(id = 456))
        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } returns newDomainRepos

        // when
        viewModel.loadMore("Kotlin")
        advanceUntilIdle()

        // then
        val finalState = states.last()
        assertFalse(finalState.isLoadingMore)
        assertEquals(2, finalState.repositories.size)
        assertEquals(newUiRepos[0], finalState.repositories[1])

        coVerify { getRepositoriesUseCase.loadRepositories(page = 2, language = "Kotlin") }

        job.cancel()
    }

    @Test
    fun `loadMore - should update loadMoreError when use case throws exception`() = runTest(testDispatcher) {
        // given
        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } returns listOf(domainGitHubRepository)

        val states = mutableListOf<MainUiState>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testDispatcher.scheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.loadList("Kotlin")
        advanceUntilIdle()

        val expectedError = RuntimeException("Load more error")
        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } throws expectedError

        // when
        viewModel.loadMore("Kotlin")
        advanceUntilIdle()

        // then
        val finalState = states.last()
        assertFalse(finalState.isLoadingMore)
        assertEquals(expectedError, finalState.loadMoreError)
        assertEquals(1, finalState.repositories.size)

        job.cancel()
    }

    @Test
    fun `loadMore - should not append items when result is empty`() = runTest(testDispatcher) {
        // given
        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } returns listOf(domainGitHubRepository)

        val states = mutableListOf<MainUiState>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testDispatcher.scheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.loadList("Kotlin")
        advanceUntilIdle()

        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } returns emptyList()

        // when
        viewModel.loadMore("Kotlin")
        advanceUntilIdle()

        // then
        val finalState = states.last()
        assertFalse(finalState.isLoadingMore)
        assertEquals(1, finalState.repositories.size)

        // Verify we tried to load page 2
        coVerify { getRepositoriesUseCase.loadRepositories(page = 2, language = "Kotlin") }

        job.cancel()
    }

    @Test
    fun `loadMore - should ignore call if already loading more`() = runTest(testDispatcher) {
        // given
        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } returns listOf(domainGitHubRepository)

        val states = mutableListOf<MainUiState>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testDispatcher.scheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.loadList("Kotlin")
        advanceUntilIdle()

        // Stub for second call to suspend
        coEvery { getRepositoriesUseCase.loadRepositories(page = 2, language = "Kotlin") } coAnswers {
            delay(100)
            listOf(domainGitHubRepository)
        }

        // when
        viewModel.loadMore("Kotlin") // Launches coroutine, updates state to isLoadingMore=true, then suspends
        runCurrent() // Force execution of the launched coroutine up to delay
        viewModel.loadMore("Kotlin") // Should see isLoadingMore=true and return

        advanceUntilIdle() // Finish the suspended coroutine

        // then
        coVerify(exactly = 1) { getRepositoriesUseCase.loadRepositories(page = 2, language = "Kotlin") }

        job.cancel()
    }

    @Test
    fun `loadMore - should retry same page when previous attempt failed`() = runTest(testDispatcher) {
        // given
        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } returns listOf(domainGitHubRepository)

        val states = mutableListOf<MainUiState>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testDispatcher.scheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.loadList("Kotlin")
        advanceUntilIdle()

        // First attempt fails
        coEvery { getRepositoriesUseCase.loadRepositories(page = 2, language = "Kotlin") } throws RuntimeException("Error")
        viewModel.loadMore("Kotlin")
        advanceUntilIdle()

        // Second attempt succeeds
        val newDomainRepos = listOf(domainGitHubRepository.copy(id = 456))
        val newUiRepos = listOf(uiRepository.copy(id = 456))
        coEvery { getRepositoriesUseCase.loadRepositories(page = 2, language = "Kotlin") } returns newDomainRepos

        // when
        viewModel.loadMore("Kotlin")
        advanceUntilIdle()

        // then
        val finalState = states.last()
        assertEquals(2, finalState.repositories.size)
        assertEquals(newUiRepos[0], finalState.repositories[1])

        coVerify(exactly = 2) { getRepositoriesUseCase.loadRepositories(page = 2, language = "Kotlin") }

        job.cancel()
    }

    @Test
    fun `loadList - should reset pagination`() = runTest(testDispatcher) {
        // given
        coEvery { getRepositoriesUseCase.loadRepositories(any(), any()) } returns listOf(domainGitHubRepository)

        val states = mutableListOf<MainUiState>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testDispatcher.scheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.loadList("Kotlin")
        advanceUntilIdle()

        // loadMore page 2
        viewModel.loadMore("Kotlin")
        advanceUntilIdle()

        // when - loadList again
        viewModel.loadList("Kotlin")
        advanceUntilIdle()

        // then - loadMore should request page 2 again (not 3)
        viewModel.loadMore("Kotlin")
        advanceUntilIdle()

        coVerify(exactly = 2) { getRepositoriesUseCase.loadRepositories(page = 1, language = "Kotlin") }
        coVerify(exactly = 2) { getRepositoriesUseCase.loadRepositories(page = 2, language = "Kotlin") }

        job.cancel()
    }
}
