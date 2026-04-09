package com.caioluis.githubpopular.data.impl.remote

import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.remote.githubpulls.service.GitHubPullRequestsService
import com.caioluis.githubpopular.data.impl.remote.githubpulls.source.PullRequestsRemoteSourceImpl
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
        val url = "https://api.test.com/repos/owner/repo/pulls"
        val page = 1
        val expected = listOf(Fixtures.createRemotePullRequest(id = 1L))

        coEvery { service.getPullRequests(url, page) } returns expected

        val result = remoteSource.fetchPullRequests(url, page)

        assertEquals(expected, result)
    }

    @Test
    fun `fetchPullRequests throws exception when service fails`() {
        val url = "https://api.test.com/repos/owner/repo/pulls"
        val page = 1
        val exception = Exception("Network error")

        coEvery { service.getPullRequests(url, page) } throws exception

        assertThrows(Exception::class.java) {
            runTest {
                remoteSource.fetchPullRequests(url, page)
            }
        }
    }
}
