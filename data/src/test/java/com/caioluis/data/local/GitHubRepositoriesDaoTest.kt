package com.caioluis.data.local

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.caioluis.data.MainDispatcherRule
import com.caioluis.data.local.Fixtures.repositories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

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
        runBlocking {
            dao.saveRepositories(repositories)

            val result = dao.getAllRepositories(1)
            assertThat(result).isNotNull
            assertThat(result).hasSize(2)
        }
    }

    @Test
    fun `assert that retrieves repos ordering by stargazers count descending from given page`() {
        runBlocking {
            dao.saveRepositories(repositories)

            val result = dao.getAllRepositories(1)
            assertThat(result).isNotNull
            assertThat(result).hasSize(2)

            assertThat(result!![0]).isEqualTo(repositories[1])
            assertThat(result[1]).isEqualTo(repositories[0])


            val resultPage2 = dao.getAllRepositories(2)

            assertThat(resultPage2).isNotNull
            assertThat(resultPage2).hasSize(1)

            assertThat(resultPage2!![0]).isEqualTo(repositories[2])
        }
    }

    @Test
    fun `assert that deletes all repositories`() {
        runBlocking {
            dao.saveRepositories(repositories)

            dao.deleteAllGitHubRepositories()

            val result = dao.getAllRepositories(1)
            assertThat(result).isEmpty()
        }
    }
}
