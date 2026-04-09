package com.caioluis.githubpopular.domain.impl.usecases

import androidx.paging.PagingData
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.repository.GitHubPullRequestsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertSame
import org.junit.Test

class GetPullRequestsUseCaseTest {
    private val gitHubPullRequestsRepository = mockk<GitHubPullRequestsRepository>()
    private val getPullRequestsUseCase = GetPullRequestsUseCaseImpl(gitHubPullRequestsRepository)

    @Test
    fun `loadPullRequests returns expected flow`() {
        val expected: Flow<PagingData<DomainGitHubPullRequest>> = flowOf(PagingData.empty())
        every {
            gitHubPullRequestsRepository.getPullRequests(
                pullUrl = "pull-url",
                repositoryId = 1,
            )
        } returns expected

        val result = getPullRequestsUseCase.loadPullRequests(
            pullUrl = "pull-url",
            repositoryId = 1,
        )

        assertSame(expected, result)
    }
}
