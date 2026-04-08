package com.caioluis.githubpopular.data.impl.local.githubpulls.dao

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
class GitHubPullRequestsDaoTest {

    private lateinit var database: GitHubReposDataBase
    private lateinit var dao: GitHubPullRequestsDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, GitHubReposDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.gitHubPullRequestsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `save and get pull requests`() = runTest {
        val repositoryId = Fixtures.REPOSITORY_ID
        val pullRequest = Fixtures.localGitHubPullRequest

        dao.savePullRequests(listOf(pullRequest))

        val result = dao.getPullRequests(repositoryId)
        assertEquals(1, result.size)
        assertEquals(pullRequest, result[0])
    }

    @Test
    fun `delete pull requests by repository id`() = runTest {
        val repositoryId = Fixtures.REPOSITORY_ID
        val pullRequest = Fixtures.localGitHubPullRequest

        dao.savePullRequests(listOf(pullRequest))
        dao.deletePullRequestsByRepositoryId(repositoryId)

        val result = dao.getPullRequests(repositoryId)
        assertEquals(0, result.size)
    }

    @Test
    fun `get pull requests returns only for specific repository`() = runTest {
        val repo1Id = 1
        val repo2Id = 2
        val pr1 = Fixtures.createLocalGitHubPullRequest(id = 1, repositoryId = repo1Id)
        val pr2 = Fixtures.createLocalGitHubPullRequest(id = 2, repositoryId = repo2Id)

        dao.savePullRequests(listOf(pr1, pr2))

        val resultRepo1 = dao.getPullRequests(repo1Id)
        assertEquals(1, resultRepo1.size)
        assertEquals(pr1, resultRepo1[0])

        val resultRepo2 = dao.getPullRequests(repo2Id)
        assertEquals(1, resultRepo2.size)
        assertEquals(pr2, resultRepo2[0])
    }
}
