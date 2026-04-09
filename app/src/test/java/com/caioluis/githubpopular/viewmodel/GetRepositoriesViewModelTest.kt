package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.caioluis.githubpopular.Constants
import com.caioluis.githubpopular.core.common.exception.AppException
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase
import com.caioluis.githubpopular.githubrepos.mapper.UiGitHubRepoMapper
import com.caioluis.githubpopular.githubrepos.viewmodel.GetRepositoriesViewModel
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
class GetRepositoriesViewModelTest {

    private val getRepositoriesUseCase: GetRepositoriesUseCase = mockk()
    private val domainMapper: UiGitHubRepoMapper = mockk()
    private val errorMapper: ErrorMapper = mockk()

    private lateinit var viewModel: GetRepositoriesViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = GetRepositoriesViewModel(
            savedStateHandle = SavedStateHandle(),
            getRepositoriesUseCase = getRepositoriesUseCase,
            domainMapper = domainMapper,
            errorMapper = errorMapper,
        )
    }

    @Test
    fun `loadList should update selectedLanguage and not fetch again if language is the same`() = runTest(mainDispatcherRule.dispatcher) {
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
    fun `loadList with different languages should fetch new data`() = runTest(mainDispatcherRule.dispatcher) {
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

    @Test
    fun `mapToAppException should return same instance when throwable is already AppException`() {
        val appException = AppException.NetworkException(Throwable("network"))

        val result = viewModel.mapToAppException(appException)

        assertEquals(appException, result)
    }

    @Test
    fun `mapToAppException should delegate to ErrorMapper for non app exceptions`() {
        val throwable = IllegalStateException("boom")
        val mapped = AppException.UnknownException(throwable)
        every { errorMapper.map(throwable) } returns mapped

        val result = viewModel.mapToAppException(throwable)

        assertEquals(mapped, result)
        verify(exactly = 1) { errorMapper.map(throwable) }
    }

    @Test
    fun `defaultLanguage should return first configured language`() {
        assertEquals(Constants.languages.first(), viewModel.defaultLanguage())
    }
}
