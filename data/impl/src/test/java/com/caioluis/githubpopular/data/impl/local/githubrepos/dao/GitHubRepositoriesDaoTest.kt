package com.caioluis.githubpopular.data.impl.local.githubrepos.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class GitHubRepositoriesDaoTest {

    private lateinit var database: GitHubReposDataBase
    private lateinit var dao: GitHubRepositoriesDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, GitHubReposDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.gitHubRepositoriesDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `save and get repositories`() = runTest {
        val page = 1
        val language = "Kotlin"
        val repository = Fixtures.localGitHubRepository

        dao.saveRepositories(listOf(repository))

        val result = dao.getAllRepositories(page, language)
        assertEquals(1, result.size)
        assertEquals(repository, result[0])
    }

    @Test
    fun `delete repos by language`() = runTest {
        val language = "Kotlin"
        val repository = Fixtures.localGitHubRepository

        dao.saveRepositories(listOf(repository))
        dao.deleteReposByLanguage(language)

        val result = dao.getAllRepositories(1, language)
        assertEquals(0, result.size)
    }

    @Test
    fun `get all repositories filters by page and language`() = runTest {
        val repo1 = Fixtures.createLocalGitHubRepository(
            id = 1,
            page = 1,
            language = "Kotlin",
            stargazersCount = 100,
        )
        val repo2 = Fixtures.createLocalGitHubRepository(
            id = 2,
            page = 2,
            language = "Kotlin",
            stargazersCount = 200,
        )
        val repo3 = Fixtures.createLocalGitHubRepository(
            id = 3,
            page = 1,
            language = "Java",
            stargazersCount = 150,
        )

        dao.saveRepositories(listOf(repo1, repo2, repo3))

        val resultPage1Kotlin = dao.getAllRepositories(1, "Kotlin")
        assertEquals(1, resultPage1Kotlin.size)
        assertEquals(repo1, resultPage1Kotlin[0])
    }

    @Test
    fun `get all repositories is case insensitive for language`() = runTest {
        val page = 1
        val language = "kotlin"
        val repository = Fixtures.createLocalGitHubRepository(
            id = 1,
            page = page,
            language = "Kotlin",
        )

        dao.saveRepositories(listOf(repository))

        val result = dao.getAllRepositories(page, language)
        assertEquals(1, result.size)
        assertEquals(repository, result[0])
    }
}
