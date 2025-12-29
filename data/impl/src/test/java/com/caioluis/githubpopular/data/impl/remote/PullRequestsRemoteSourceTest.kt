package com.caioluis.githubpopular.data.impl.remote

import com.caioluis.githubpopular.data.bridge.remote.model.RemotePullRequest
import com.caioluis.githubpopular.data.impl.remote.service.GitHubPullRequestsService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PullRequestsRemoteSourceTest {
    private val service = mockk<GitHubPullRequestsService>()
    private val remoteSource = PullRequestsRemoteSourceImpl(service)

    @Test
    fun `fetchPullRequests returns expected result`() = runTest {
        // Arrange
        val url = "https://api.github.com/repos/owner/repo/pulls"
        val expected = listOf(RemotePullRequest(id = 1, title = "PR 1"))

        coEvery { service.getPullRequests(url) } returns expected

        // Act
        val result = remoteSource.fetchPullRequests(url)

        // Assert
        assertEquals(expected, result)
    }

    @Test(expected = Exception::class)
    fun `fetchPullRequests throws exception when service fails`() = runTest {
        // Arrange
        val url = "https://api.github.com/repos/owner/repo/pulls"
        val exception = Exception("Network error")

        coEvery { service.getPullRequests(url) } throws exception

        // Act
        remoteSource.fetchPullRequests(url)
    }
}
