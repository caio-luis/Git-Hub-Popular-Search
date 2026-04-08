package com.caioluis.githubpopular.data.impl.local.githubrepos.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
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
class RemoteKeysDaoTest {

    private lateinit var database: GitHubReposDataBase
    private lateinit var dao: RemoteKeysDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GitHubReposDataBase::class.java,
        ).allowMainThreadQueries().build()

        dao = database.remoteKeysDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `insertOrReplace should insert data successfully`() = runTest {
        val remoteKey = Fixtures.createRemoteKey()

        dao.insertOrReplace(remoteKey)

        val result = dao.remoteKeyByQuery(Fixtures.DEFAULT_LANGUAGE)
        assertEquals(remoteKey, result)
    }

    @Test
    fun `insertOrReplace should replace data on conflict`() = runTest {
        val initialKey = Fixtures.createRemoteKey(nextPage = 2)
        val updatedKey = Fixtures.createRemoteKey(nextPage = 3)

        dao.insertOrReplace(initialKey)
        dao.insertOrReplace(updatedKey)

        val result = dao.remoteKeyByQuery(Fixtures.DEFAULT_LANGUAGE)
        assertEquals(3, result?.nextPage)
    }

    @Test
    fun `remoteKeyByQuery should return null when key does not exist`() = runTest {
        val result = dao.remoteKeyByQuery("UnknownLanguage")

        assertNull(result)
    }

    @Test
    fun `deleteByQuery should remove only the specified key`() = runTest {
        val key1 = Fixtures.createRemoteKey(language = "Kotlin", nextPage = 2)
        val key2 = Fixtures.createRemoteKey(language = "Java", nextPage = 2)

        dao.insertOrReplace(key1)
        dao.insertOrReplace(key2)

        dao.deleteByQuery("Kotlin")

        val resultDeleted = dao.remoteKeyByQuery("Kotlin")
        val resultKept = dao.remoteKeyByQuery("Java")

        assertNull(resultDeleted)
        assertEquals(key2, resultKept)
    }
}
