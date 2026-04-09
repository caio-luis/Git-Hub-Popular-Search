package com.caioluis.githubpopular.data.impl.local.githubpulls.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.local.PullRequestRemoteKeysDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class PullRequestRemoteKeysDaoTest {

    private lateinit var database: GitHubReposDataBase
    private lateinit var dao: PullRequestRemoteKeysDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GitHubReposDataBase::class.java,
        ).allowMainThreadQueries().build()

        dao = database.pullRequestRemoteKeysDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `insertOrReplace should insert data successfully`() = runTest {
        val remoteKey = Fixtures.createPullRequestRemoteKey()

        dao.insertOrReplace(remoteKey)

        val result = dao.remoteKeyByRepositoryId(Fixtures.REPOSITORY_ID)
        assertEquals(remoteKey, result)
    }

    @Test
    fun `insertOrReplace should replace data on conflict`() = runTest {
        val initialKey = Fixtures.createPullRequestRemoteKey(nextPage = 2)
        val updatedKey = Fixtures.createPullRequestRemoteKey(nextPage = 3)

        dao.insertOrReplace(initialKey)
        dao.insertOrReplace(updatedKey)

        val result = dao.remoteKeyByRepositoryId(Fixtures.REPOSITORY_ID)
        assertEquals(3, result?.nextPage)
    }

    @Test
    fun `remoteKeyByRepositoryId should return null when key does not exist`() = runTest {
        val result = dao.remoteKeyByRepositoryId(repositoryId = 999)

        assertNull(result)
    }

    @Test
    fun `deleteByRepositoryId should remove only the specified key`() = runTest {
        val key1 = Fixtures.createPullRequestRemoteKey(repositoryId = 1, nextPage = 2)
        val key2 = Fixtures.createPullRequestRemoteKey(repositoryId = 2, nextPage = 2)

        dao.insertOrReplace(key1)
        dao.insertOrReplace(key2)

        dao.deleteByRepositoryId(repositoryId = 1)

        val resultDeleted = dao.remoteKeyByRepositoryId(repositoryId = 1)
        val resultKept = dao.remoteKeyByRepositoryId(repositoryId = 2)

        assertNull(resultDeleted)
        assertEquals(key2, resultKept)
    }
}
