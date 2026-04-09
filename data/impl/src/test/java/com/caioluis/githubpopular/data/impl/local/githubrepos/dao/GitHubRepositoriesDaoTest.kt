package com.caioluis.githubpopular.data.impl.local.githubrepos.dao

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GitHubRepositoriesDaoTest {

    private lateinit var database: GitHubReposDataBase
    private lateinit var dao: GitHubRepositoriesDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GitHubReposDataBase::class.java,
        ).allowMainThreadQueries().build()

        dao = database.gitHubRepositoriesDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `saveRepositories should insert data successfully`() = runTest {
        val repository = Fixtures.createLocalGitHubRepository()

        dao.saveRepositories(listOf(repository))

        val result = loadPagedData(Fixtures.DEFAULT_LANGUAGE)
        assertEquals(1, result.size)
        assertEquals(repository, result.first())
    }

    @Test
    fun `saveRepositories should replace data on conflict`() = runTest {
        val initialRepository =
            Fixtures.createLocalGitHubRepository(id = 1).copy(title = "Old Title")
        val updatedRepository =
            Fixtures.createLocalGitHubRepository(id = 1).copy(title = "New Title")

        dao.saveRepositories(listOf(initialRepository))
        dao.saveRepositories(listOf(updatedRepository))

        val result = loadPagedData(Fixtures.DEFAULT_LANGUAGE)
        assertEquals(1, result.size)
        assertEquals("New Title", result.first().title)
    }

    @Test
    fun `getPagedRepositories should return data ordered by stargazersCount descending`() = runTest {
        val repoWith10Stars =
            Fixtures.createLocalGitHubRepository(id = 1).copy(stargazersCount = 10)
        val repoWith50Stars =
            Fixtures.createLocalGitHubRepository(id = 2).copy(stargazersCount = 50)
        val repoWith30Stars =
            Fixtures.createLocalGitHubRepository(id = 3).copy(stargazersCount = 30)

        dao.saveRepositories(listOf(repoWith10Stars, repoWith50Stars, repoWith30Stars))

        val result = loadPagedData(Fixtures.DEFAULT_LANGUAGE)

        assertEquals(3, result.size)
        assertEquals(repoWith50Stars.id, result[0].id)
        assertEquals(repoWith30Stars.id, result[1].id)
        assertEquals(repoWith10Stars.id, result[2].id)
    }

    @Test
    fun `getPagedRepositories should return only selected language data`() = runTest {
        val kotlinRepo = Fixtures.createLocalGitHubRepository(id = 1, language = "Kotlin")
        val swiftRepo = Fixtures.createLocalGitHubRepository(id = 2, language = "Swift")

        dao.saveRepositories(listOf(kotlinRepo, swiftRepo))

        val result = loadPagedData("Swift")
        assertEquals(1, result.size)
        assertEquals(swiftRepo.id, result.first().id)
    }

    @Test
    fun `clearRepositories should delete only selected language data`() = runTest {
        val repositories = listOf(
            Fixtures.createLocalGitHubRepository(id = 1, language = "Kotlin"),
            Fixtures.createLocalGitHubRepository(id = 2, language = "Swift"),
        )
        dao.saveRepositories(repositories)

        dao.clearRepositories("Kotlin")

        val kotlinResult = loadPagedData("Kotlin")
        val swiftResult = loadPagedData("Swift")

        assertTrue(kotlinResult.isEmpty())
        assertEquals(1, swiftResult.size)
        assertEquals(2, swiftResult.first().id)
    }

    private suspend fun loadPagedData(language: String): List<LocalGitHubRepository> {
        val pagingSource = dao.getPagedRepositories(language)
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false,
            ),
        )

        assertTrue(loadResult is PagingSource.LoadResult.Page)
        return (loadResult as PagingSource.LoadResult.Page).data
    }
}
