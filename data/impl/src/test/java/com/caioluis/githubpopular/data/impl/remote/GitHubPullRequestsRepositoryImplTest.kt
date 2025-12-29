package com.caioluis.githubpopular.data.impl.remote

import com.caioluis.githubpopular.core.common.utils.LogUtil
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.githubpullrequests.PullRequestsLocalSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GitHubPullRequestsRepositoryImplTest {

    private val remoteSource = mockk<PullRequestsRemoteSource>()
    private val localSource = mockk<PullRequestsLocalSource>()
    private lateinit var repository: GitHubPullRequestsRepositoryImpl

    @Before
    fun setUp() {
        repository = GitHubPullRequestsRepositoryImpl(remoteSource, localSource)
        mockkObject(LogUtil)
        every { LogUtil.e(any(), any(), any()) } just Runs
    }

    @After
    fun tearDown() {
        unmockkObject(LogUtil)
    }

    @Test
    fun `getPullRequests returns data from remote and saves to local`() = runTest {
        // Arrange
        val pullUrl = "http://pull-url"
        val repositoryId = 1
        val remotePullRequest = Fixtures.createRemotePullRequest()
        val domainPullRequest = Fixtures.domainGitHubPullRequest
        val expected = listOf(domainPullRequest)
        val remoteList = listOf(remotePullRequest)

        coEvery { remoteSource.fetchPullRequests(pullUrl) } returns remoteList
        coEvery { localSource.saveToLocalCache(any(), any()) } just Runs

        // Act
        val result = repository.getPullRequests(pullUrl, repositoryId)

        // Assert
        assertEquals(expected.first().id, result.first().id)
        assertEquals(expected.first().title, result.first().title)
        coVerify(exactly = 1) { remoteSource.fetchPullRequests(pullUrl) }
        coVerify(exactly = 1) { localSource.saveToLocalCache(any(), repositoryId) }
    }

    @Test
    fun `getPullRequests returns data from local when remote fails`() = runTest {
        // Arrange
        val pullUrl = "http://pull-url"
        val repositoryId = 1
        val expected = listOf(Fixtures.domainGitHubPullRequest)

        coEvery { remoteSource.fetchPullRequests(pullUrl) } throws Exception("Remote error")
        coEvery { localSource.getFromCache(repositoryId) } returns expected

        // Act
        val result = repository.getPullRequests(pullUrl, repositoryId)

        // Assert
        assertEquals(expected, result)
        coVerify(exactly = 1) { remoteSource.fetchPullRequests(pullUrl) }
        coVerify(exactly = 1) { localSource.getFromCache(repositoryId) }
    }

    @Test(expected = Exception::class)
    fun `getPullRequests throws exception when both remote and local fail`() = runTest {
        // Arrange
        val pullUrl = "http://pull-url"
        val repositoryId = 1
        val exception = Exception("Remote error")

        coEvery { remoteSource.fetchPullRequests(pullUrl) } throws exception
        coEvery { localSource.getFromCache(repositoryId) } returns emptyList()

        // Act
        repository.getPullRequests(pullUrl, repositoryId)
    }

    @Test
    fun `getPullRequests returns data from local when remote returns empty list`() = runTest {
        // Arrange
        val pullUrl = "http://pull-url"
        val repositoryId = 1
        val expected = listOf(Fixtures.domainGitHubPullRequest)

        coEvery { remoteSource.fetchPullRequests(pullUrl) } returns emptyList()
        coEvery { localSource.getFromCache(repositoryId) } returns expected

        // Act
        val result = repository.getPullRequests(pullUrl, repositoryId)

        // Assert
        assertEquals(expected, result)
        coVerify(exactly = 1) { remoteSource.fetchPullRequests(pullUrl) }
        coVerify(exactly = 1) { localSource.getFromCache(repositoryId) }
    }
}
