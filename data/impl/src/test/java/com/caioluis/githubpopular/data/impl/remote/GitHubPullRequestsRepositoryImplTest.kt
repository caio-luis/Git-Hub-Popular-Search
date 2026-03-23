package com.caioluis.githubpopular.data.impl.remote

import android.util.Log
import com.caioluis.githubpopular.core.common.utils.LogUtil
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.githubpullrequests.PullRequestsLocalSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GitHubPullRequestsRepositoryImplTest {

    private val remoteSource = mockk<PullRequestsRemoteSource>()
    private val localSource = mockk<PullRequestsLocalSource>()
    private lateinit var repository: GitHubPullRequestsRepositoryImpl

    @Before
    fun setUp() {
        repository = GitHubPullRequestsRepositoryImpl(remoteSource, localSource)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.w(any(), any<String>(), any()) } returns 0

        LogUtil.isDebug = true
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `getPullRequests returns data from remote and saves to local`() = runTest {
        // Arrange
        val pullUrl = "http://pull-url"
        val repositoryId = 1
        val page = 1
        val remotePullRequest = Fixtures.createRemotePullRequest()
        val domainPullRequest = Fixtures.domainGitHubPullRequest
        val expected = listOf(domainPullRequest)
        val remoteList = listOf(remotePullRequest)

        coEvery { remoteSource.fetchPullRequests(pullUrl, page) } returns remoteList
        coEvery { localSource.saveToLocalCache(any(), any()) } returns Unit

        // Act
        val result = repository.getPullRequests(pullUrl, repositoryId, page)

        // Assert
        assertEquals(expected.first().id, result.first().id)
        assertEquals(expected.first().title, result.first().title)
        coVerify(exactly = 1) { remoteSource.fetchPullRequests(pullUrl, page) }
        coVerify(exactly = 1) { localSource.saveToLocalCache(any(), repositoryId) }
    }

    @Test
    fun `getPullRequests returns data from local when remote fails`() = runTest {
        // Arrange
        val pullUrl = "http://pull-url"
        val repositoryId = 1
        val page = 1
        val expected = listOf(Fixtures.domainGitHubPullRequest)

        coEvery { remoteSource.fetchPullRequests(pullUrl, page) } throws Exception("Remote error")
        coEvery { localSource.getFromCache(repositoryId) } returns expected

        // Act
        val result = repository.getPullRequests(pullUrl, repositoryId, page)

        // Assert
        assertEquals(expected, result)
        coVerify(exactly = 1) { remoteSource.fetchPullRequests(pullUrl, page) }
        coVerify(exactly = 1) { localSource.getFromCache(repositoryId) }
    }

    @Test(expected = Exception::class)
    fun `getPullRequests throws exception when both remote and local fail`() = runTest {
        // Arrange
        val pullUrl = "http://pull-url"
        val repositoryId = 1
        val page = 1
        val exception = Exception("Remote error")

        coEvery { remoteSource.fetchPullRequests(pullUrl, page) } throws exception
        coEvery { localSource.getFromCache(repositoryId) } returns emptyList()

        // Act
        repository.getPullRequests(pullUrl, repositoryId, page)
    }

    @Test
    fun `getPullRequests returns empty list when remote returns empty list`() = runTest {
        // Arrange
        val pullUrl = "http://pull-url"
        val repositoryId = 1
        val page = 1

        coEvery { remoteSource.fetchPullRequests(pullUrl, page) } returns emptyList()

        // Act
        val result = repository.getPullRequests(pullUrl, repositoryId, page)

        // Assert
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { remoteSource.fetchPullRequests(pullUrl, page) }
        coVerify(exactly = 0) { localSource.getFromCache(any()) }
    }
}
