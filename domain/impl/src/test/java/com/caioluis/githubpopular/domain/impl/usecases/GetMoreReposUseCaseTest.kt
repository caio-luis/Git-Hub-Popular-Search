package com.caioluis.githubpopular.domain.impl.usecases

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import com.caioluis.githubpopular.domain.impl.usecases.GetMoreReposUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMoreReposUseCaseTest {

    private val gitHubReposRepository = mockk<GitHubReposRepository>()
    private val getMoreReposUseCase = GetMoreReposUseCaseImpl(gitHubReposRepository)

    @Test
    fun `loadRepositories returns expected result`() = runTest {
        // Arrange
        val expected = listOf(DomainGitHubRepository())
        coEvery { gitHubReposRepository.getGitHubRepositories(any(), any()) } returns flowOf(
            expected
        )

        // Act
        val result = getMoreReposUseCase.loadRepositories("Kotlin")

        // Assert
        assertEquals(Result.success(expected), result)
    }

    @Test
    fun `loadRepositories returns failure`() = runTest {
        // Arrange
        val exception = Exception()
        val expected = Result.failure<List<DomainGitHubRepository>>(exception)

        coEvery { gitHubReposRepository.getGitHubRepositories(any(), any()) } throws exception

        // Act
        val result = getMoreReposUseCase.loadRepositories("Kotlin")

        // Assert
        assertEquals(expected, result)
    }
}
