package com.caioluis.githubpopular.data.impl.local.githubrepos.source

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.local.githubrepos.dao.GitHubReposRemoteKeysDao
import com.caioluis.githubpopular.data.impl.local.githubrepos.dao.GitHubRepositoriesDao
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
class GithubReposLocalSourceImplTest {

    private val localDatabase: GitHubReposDataBase = mockk()
    private val repositoriesDao: GitHubRepositoriesDao = mockk(relaxed = true)
    private val remoteKeysDao: GitHubReposRemoteKeysDao = mockk(relaxed = true)

    private lateinit var localSource: GithubReposLocalSourceImpl

    @Before
    fun setup() {
        mockkStatic("androidx.room.RoomDatabaseKt")

        localSource = GithubReposLocalSourceImpl(
            localDatabase = localDatabase,
            repositoriesDao = repositoriesDao,
            remoteKeysDao = remoteKeysDao,
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getRemoteKey should delegate to remoteKeysDao`() = runTest {
        val expected = Fixtures.createRemoteKey(nextPage = 2)
        coEvery { remoteKeysDao.remoteKeyByQuery(Fixtures.DEFAULT_LANGUAGE) } returns expected

        val result = localSource.getRemoteKey(Fixtures.DEFAULT_LANGUAGE)

        assertEquals(expected, result)
        coVerify(exactly = 1) { remoteKeysDao.remoteKeyByQuery(Fixtures.DEFAULT_LANGUAGE) }
    }

    @Test
    fun `getRemoteKey should return null when no key exists`() = runTest {
        coEvery { remoteKeysDao.remoteKeyByQuery(Fixtures.DEFAULT_LANGUAGE) } returns null

        val result = localSource.getRemoteKey(Fixtures.DEFAULT_LANGUAGE)

        assertNull(result)
    }

    @Test
    fun `deleteRemoteKey should delegate to remoteKeysDao`() = runTest {
        localSource.deleteRemoteKey(Fixtures.DEFAULT_LANGUAGE)

        coVerify(exactly = 1) { remoteKeysDao.deleteByQuery(Fixtures.DEFAULT_LANGUAGE) }
    }

    @Test
    fun `insertRemoteKey should delegate to remoteKeysDao`() = runTest {
        val remoteKey = Fixtures.createRemoteKey(nextPage = 3)

        localSource.insertRemoteKey(remoteKey)

        coVerify(exactly = 1) { remoteKeysDao.insertOrReplace(remoteKey) }
    }

    @Test
    fun `saveRepositories should delegate to repositoriesDao`() = runTest {
        val repositories = listOf(Fixtures.createLocalGitHubRepository())

        localSource.saveRepositories(repositories)

        coVerify(exactly = 1) { repositoriesDao.saveRepositories(repositories) }
    }

    @Test
    fun `clearRepositories should delegate to repositoriesDao`() = runTest {
        localSource.clearRepositories(Fixtures.DEFAULT_LANGUAGE)

        coVerify(exactly = 1) { repositoriesDao.clearRepositories(Fixtures.DEFAULT_LANGUAGE) }
    }

    @Test
    fun `getPagedRepositories should delegate to repositoriesDao`() {
        val pagingSource = mockk<PagingSource<Int, LocalGitHubRepository>>()
        every { repositoriesDao.getPagedRepositories(Fixtures.DEFAULT_LANGUAGE) } returns pagingSource

        val result = localSource.getPagedRepositories(Fixtures.DEFAULT_LANGUAGE)

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
