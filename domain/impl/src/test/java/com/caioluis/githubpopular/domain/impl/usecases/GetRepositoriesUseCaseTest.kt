package com.caioluis.githubpopular.domain.impl.usecases

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetRepositoriesUseCaseTest {
    private val gitHubReposRepository = mockk<GitHubReposRepository>()
    private val getReposUseCase = GetRepositoriesUseCaseImpl(gitHubReposRepository)

    @Test
    fun `loadRepositories returns expected result`() =
        runTest {
            // Arrange
            val expected = listOf(DomainGitHubRepository())
            coEvery { gitHubReposRepository.getGitHubRepositories(any(), any()) } returns
                flowOf(
                    expected,
                )

            // Act
            val result = getReposUseCase.loadRepositories("Kotlin").first()

            // Assert
            assertEquals(expected, result)
        }

    @Test(expected = Exception::class)
    fun `loadRepositories returns failure`() =
        runTest {
            // Arrange
            val exception = Exception()

            coEvery { gitHubReposRepository.getGitHubRepositories(any(), any()) } throws exception

            // Act
            getReposUseCase.loadRepositories("Kotlin")
        }
}
