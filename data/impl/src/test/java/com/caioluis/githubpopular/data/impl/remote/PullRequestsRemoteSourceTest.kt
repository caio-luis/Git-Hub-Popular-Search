package com.caioluis.githubpopular.data.impl.remote

import com.caioluis.githubpopular.data.bridge.remote.model.RemotePullRequest
import com.caioluis.githubpopular.data.impl.Fixtures
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
        val page = 1
        val expected = listOf(Fixtures.createRemotePullRequest(id = 1L))

        coEvery { service.getPullRequests(url, page) } returns expected

        // Act
        val result = remoteSource.fetchPullRequests(url, page)

        // Assert
        assertEquals(expected, result)
    }

    @Test(expected = Exception::class)
    fun `fetchPullRequests throws exception when service fails`() = runTest {
        // Arrange
        val url = "https://api.github.com/repos/owner/repo/pulls"
        val page = 1
        val exception = Exception("Network error")

        coEvery { service.getPullRequests(url, page) } throws exception

        // Act
        remoteSource.fetchPullRequests(url, page)
    }
}
