package com.caioluis.githubpopular.data.impl.local.githubpullrequests

import com.caioluis.githubpopular.data.impl.local.Fixtures
import com.caioluis.githubpopular.data.impl.local.githubpullrequests.dao.GitHubPullRequestsDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PullRequestsLocalSourceImplTest {

    private val gitHubPullRequestsDao: GitHubPullRequestsDao = mockk(relaxed = true)
    private val localSource = PullRequestsLocalSourceImpl(gitHubPullRequestsDao)

    @Test
    fun `saveToLocalCache should delete old pull requests and save new ones`() = runTest {
        val repositoryId = Fixtures.REPOSITORY_ID
        val pullRequests = listOf(Fixtures.domainGitHubPullRequest)
        val localPullRequests = listOf(Fixtures.localGitHubPullRequest)

        localSource.saveToLocalCache(pullRequests, repositoryId)

        coVerify { gitHubPullRequestsDao.deletePullRequestsByRepositoryId(repositoryId) }
        coVerify { gitHubPullRequestsDao.savePullRequests(localPullRequests) }
    }

    @Test
    fun `getFromCache should return pull requests from dao`() = runTest {
        val repositoryId = Fixtures.REPOSITORY_ID
        val localPullRequests = listOf(Fixtures.localGitHubPullRequest)
        val expectedDomainPullRequests = listOf(Fixtures.domainGitHubPullRequest)

        coEvery { gitHubPullRequestsDao.getPullRequests(repositoryId) } returns localPullRequests

        val result = localSource.getFromCache(repositoryId)

        assertEquals(expectedDomainPullRequests, result)
    }
}
