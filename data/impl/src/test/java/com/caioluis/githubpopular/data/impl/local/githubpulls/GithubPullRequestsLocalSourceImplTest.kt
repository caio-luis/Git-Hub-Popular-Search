package com.caioluis.githubpopular.data.impl.local.githubpulls

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.local.PullRequestRemoteKeysDao
import com.caioluis.githubpopular.data.impl.local.githubpulls.dao.GitHubPullRequestsDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GithubPullRequestsLocalSourceImplTest {

    private val localDatabase: GitHubReposDataBase = mockk()
    private val pullRequestsDao: GitHubPullRequestsDao = mockk(relaxed = true)
    private val pullRequestRemoteKeysDao: PullRequestRemoteKeysDao = mockk(relaxed = true)

    private lateinit var localSource: GithubPullRequestsLocalSourceImpl

    @Before
    fun setup() {
        mockkStatic("androidx.room.RoomDatabaseKt")

        localSource = GithubPullRequestsLocalSourceImpl(
            localDatabase = localDatabase,
            pullRequestsDao = pullRequestsDao,
            pullRequestRemoteKeysDao = pullRequestRemoteKeysDao,
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getRemoteKey should delegate to pullRequestRemoteKeysDao`() = runTest {
        val expected = Fixtures.createPullRequestRemoteKey(nextPage = 2)
        coEvery { pullRequestRemoteKeysDao.remoteKeyByRepositoryId(Fixtures.REPOSITORY_ID) } returns expected

        val result = localSource.getRemoteKey(Fixtures.REPOSITORY_ID)

        assertEquals(expected, result)
        coVerify(exactly = 1) { pullRequestRemoteKeysDao.remoteKeyByRepositoryId(Fixtures.REPOSITORY_ID) }
    }

    @Test
    fun `getRemoteKey should return null when no key exists`() = runTest {
        coEvery { pullRequestRemoteKeysDao.remoteKeyByRepositoryId(Fixtures.REPOSITORY_ID) } returns null

        val result = localSource.getRemoteKey(Fixtures.REPOSITORY_ID)

        assertNull(result)
    }

    @Test
    fun `deleteRemoteKey should delegate to pullRequestRemoteKeysDao`() = runTest {
        localSource.deleteRemoteKey(Fixtures.REPOSITORY_ID)

        coVerify(exactly = 1) { pullRequestRemoteKeysDao.deleteByRepositoryId(Fixtures.REPOSITORY_ID) }
    }

    @Test
    fun `insertRemoteKey should delegate to pullRequestRemoteKeysDao`() = runTest {
        val remoteKey = Fixtures.createPullRequestRemoteKey(nextPage = 3)

        localSource.insertRemoteKey(remoteKey)

        coVerify(exactly = 1) { pullRequestRemoteKeysDao.insertOrReplace(remoteKey) }
    }

    @Test
    fun `savePullRequests should delegate to pullRequestsDao`() = runTest {
        val pullRequests = listOf(Fixtures.createLocalGitHubPullRequest())

        localSource.savePullRequests(pullRequests)

        coVerify(exactly = 1) { pullRequestsDao.savePullRequests(pullRequests) }
    }

    @Test
    fun `deletePullRequestsByRepositoryId should delegate to pullRequestsDao`() = runTest {
        localSource.deletePullRequestsByRepositoryId(Fixtures.REPOSITORY_ID)

        coVerify(exactly = 1) { pullRequestsDao.deletePullRequestsByRepositoryId(Fixtures.REPOSITORY_ID) }
    }

    @Test
    fun `countPullRequestsByRepositoryId should delegate to pullRequestsDao`() = runTest {
        coEvery { pullRequestsDao.countPullRequestsByRepositoryId(Fixtures.REPOSITORY_ID) } returns 5

        val result = localSource.countPullRequestsByRepositoryId(Fixtures.REPOSITORY_ID)

        assertEquals(5, result)
        coVerify(exactly = 1) { pullRequestsDao.countPullRequestsByRepositoryId(Fixtures.REPOSITORY_ID) }
    }

    @Test
    fun `getPagedPullRequests should delegate to pullRequestsDao`() {
        val pagingSource = mockk<PagingSource<Int, LocalGitHubPullRequest>>()
        every { pullRequestsDao.getPagedPullRequests(Fixtures.REPOSITORY_ID) } returns pagingSource

        val result = localSource.getPagedPullRequests(Fixtures.REPOSITORY_ID)

        assertEquals(pagingSource, result)
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `withTransaction should execute block via database transaction`() = runTest {
        coEvery {
            localDatabase.withTransaction(any<suspend () -> Unit>())
        } coAnswers {
            val block = args[1] as suspend () -> Unit
            block()
        }

        var executed = false
        localSource.withTransaction { executed = true }

        assertEquals(true, executed)
    }
}
