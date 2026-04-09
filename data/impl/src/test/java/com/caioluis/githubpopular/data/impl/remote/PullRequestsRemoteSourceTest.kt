package com.caioluis.githubpopular.data.impl.remote

import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.remote.githubpullrequests.PullRequestsRemoteSourceImpl
import com.caioluis.githubpopular.data.impl.remote.githubpulls.service.GitHubPullRequestsService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
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

    @Test
    fun `fetchPullRequests throws exception when service fails`() {
        // Arrange
        val url = "https://api.github.com/repos/owner/repo/pulls"
        val page = 1
        val exception = Exception("Network error")

        coEvery { service.getPullRequests(url, page) } throws exception

        // Act + Assert
        assertThrows(Exception::class.java) {
            runTest {
                remoteSource.fetchPullRequests(url, page)
            }
        }
    }
}
