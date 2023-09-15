package com.caioluis.githubpopular.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.caioluis.githubpopular.data.MainDispatcherRule
import com.caioluis.githubpopular.data.local.Fixtures.repositories
import com.caioluis.githubpopular.data.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.local.GitHubRepositoriesDao
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class GitHubRepositoriesDaoTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var db: GitHubReposDataBase
    private lateinit var dao: GitHubRepositoriesDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GitHubReposDataBase::class.java).build()
        dao = db.gitHubRepositoriesDao()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun `assert that save repositories`() {
        runTest {
            dao.saveRepositories(repositories)

            val result1 = dao.getAllRepositories(1, "kotlin")
            val result2 = dao.getAllRepositories(2, "kotlin")
            val result3 = dao.getAllRepositories(1, "c++")

            assertNotNull(result1)
            assertNotNull(result2)
            assertNotNull(result3)

            assertEquals(result1?.size, 2)
            assertEquals(result2?.size, 1)
            assertEquals(result3?.size, 1)
        }
    }

    @Test
    fun `assert that returns repos ordering by stargazers count descending from given page`() {
        runTest {
            dao.saveRepositories(repositories)

            val result = dao.getAllRepositories(1, "kotlin")
            assertNotNull(result)
            assertEquals(result?.size, 2)

            assertEquals(repositories[1], result?.first())
            assertEquals(repositories.first(), result!![1])

            val resultPage2 = dao.getAllRepositories(2, "kotlin")

            assertNotNull(resultPage2)
            assertEquals(resultPage2?.size, 1)
            assertEquals(repositories[2], resultPage2?.first())
        }
    }

    @Test
    fun `assert that returns repos from given language`() {
        runTest {
            dao.saveRepositories(repositories)

            val result = dao.getAllRepositories(1, "kotlin")
            assertNotNull(result)
            assertEquals(result?.size, 2)

            assertEquals(repositories[1], result?.first())
            assertEquals(repositories.first(), result!![1])

            val resultPage2 = dao.getAllRepositories(1, "c++")

            assertNotNull(resultPage2)
            assertEquals(resultPage2?.size, 1)

            assertEquals(repositories[3], resultPage2?.first())
        }
    }

    @Test
    fun `assert that returns repos from given language ignoring case`() {
        runTest {
            dao.saveRepositories(repositories)

            val result = dao.getAllRepositories(1, "Kotlin")

            assertNotNull(result)
            assertEquals(result?.size, 2)

            assertEquals(repositories[1], result?.first())
            assertEquals(repositories.first(), result!![1])
        }
    }

    @Test
    fun `assert that deletes all repositories by language`() {
        runTest {
            dao.saveRepositories(repositories)

            dao.deleteReposByLanguage("kotlin")

            val result = dao.getAllRepositories(1, "kotlin")
            val result2 = dao.getAllRepositories(1, "c++")

            assertTrue(result?.isEmpty()!!)
            assertTrue(result2?.isNotEmpty()!!)
        }
    }

    @Test
    fun `assert that deletes all repositories by language ignoring case`() {
        runTest {
            dao.saveRepositories(repositories)

            dao.deleteReposByLanguage("Kotlin")

            val result = dao.getAllRepositories(1, "kotlin")
            val result2 = dao.getAllRepositories(1, "c++")

            assertTrue(result?.isEmpty()!!)
            assertTrue(result2?.isNotEmpty()!!)
        }
    }
}
