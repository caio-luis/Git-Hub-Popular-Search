package com.caioluis.githubpopular.domain.impl.usecases

import androidx.paging.PagingData
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertSame
import org.junit.Test

class GetRepositoriesUseCaseTest {
    private val gitHubReposRepository = mockk<GitHubReposRepository>()
    private val getReposUseCase = GetRepositoriesUseCaseImpl(gitHubReposRepository)

    @Test
    fun `loadRepositories returns expected flow`() {
        val expected: Flow<PagingData<DomainGitHubRepository>> = flowOf(PagingData.empty())
        every { gitHubReposRepository.getGitHubRepositories("Kotlin") } returns expected

        val result = getReposUseCase.loadRepositories("Kotlin")

        assertSame(expected, result)
    }
}
