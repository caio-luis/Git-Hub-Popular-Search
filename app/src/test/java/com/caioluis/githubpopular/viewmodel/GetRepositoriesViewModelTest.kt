package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.githubrepos.mapper.UiGitHubRepoMapper
import com.caioluis.githubpopular.githubrepos.viewmodel.GetRepositoriesViewModel
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetRepositoriesViewModelTest {

    private val getRepositoriesUseCase: GetRepositoriesUseCase = mockk()
    private val domainMapper: UiGitHubRepoMapper = mockk()
    private val errorMapper: ErrorMapper = mockk()

    private lateinit var viewModel: GetRepositoriesViewModel

    private val unconfinedDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(unconfinedDispatcher)
        viewModel = GetRepositoriesViewModel(
            savedStateHandle = SavedStateHandle(),
            getRepositoriesUseCase = getRepositoriesUseCase,
            domainMapper = domainMapper,
            errorMapper = errorMapper,
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadList should update selectedLanguage and not fetch again if language is the same`() = runTest(unconfinedDispatcher) {
        val language = "Kotlin"
        every { getRepositoriesUseCase.loadRepositories(any()) } returns flowOf(PagingData.empty())

        val job = backgroundScope.launch {
            viewModel.repositories.collect {}
        }

        viewModel.loadList(language)
        viewModel.loadList(language)

        assertEquals(language, viewModel.selectedLanguage.value)
        verify(exactly = 1) { getRepositoriesUseCase.loadRepositories(language) }

        job.cancel()
    }

    @Test
    fun `loadList with different languages should fetch new data`() = runTest(unconfinedDispatcher) {
        val initialLanguage = "Java"
        val newLanguage = "Kotlin"
        every { getRepositoriesUseCase.loadRepositories(any()) } returns flowOf(PagingData.empty())

        val job = backgroundScope.launch {
            viewModel.repositories.collect {}
        }

        viewModel.loadList(initialLanguage)
        viewModel.loadList(newLanguage)

        assertEquals(newLanguage, viewModel.selectedLanguage.value)
        verify(exactly = 1) { getRepositoriesUseCase.loadRepositories(initialLanguage) }
        verify(exactly = 1) { getRepositoriesUseCase.loadRepositories(newLanguage) }

        job.cancel()
    }
}
