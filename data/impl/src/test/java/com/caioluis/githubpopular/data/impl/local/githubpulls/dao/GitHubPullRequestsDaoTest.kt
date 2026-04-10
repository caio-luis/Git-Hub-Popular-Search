package com.caioluis.githubpopular.data.impl.local.githubpulls.dao

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config
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

        val result = loadPagedData(repositoryId)
        assertEquals(1, result.size)
        assertEquals(pullRequest, result[0])
    }

    @Test
    fun `delete pull requests by repository id`() = runTest {
        val repositoryId = Fixtures.REPOSITORY_ID
        val pullRequest = Fixtures.localGitHubPullRequest

        dao.savePullRequests(listOf(pullRequest))
        dao.deletePullRequestsByRepositoryId(repositoryId)

        val result = loadPagedData(repositoryId)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `get pull requests returns only for specific repository`() = runTest {
        val repo1Id = 1
        val repo2Id = 2
        val pr1 = Fixtures.createLocalGitHubPullRequest(id = 1, repositoryId = repo1Id)
        val pr2 = Fixtures.createLocalGitHubPullRequest(id = 2, repositoryId = repo2Id)

        dao.savePullRequests(listOf(pr1, pr2))

        val resultRepo1 = loadPagedData(repo1Id)
        assertEquals(1, resultRepo1.size)
        assertEquals(pr1, resultRepo1[0])

        val resultRepo2 = loadPagedData(repo2Id)
        assertEquals(1, resultRepo2.size)
        assertEquals(pr2, resultRepo2[0])
    }

    @Test
    fun `get pull requests returns items ordered by page and position`() = runTest {
        val repositoryId = Fixtures.REPOSITORY_ID
        val pageTwoItem = Fixtures.createLocalGitHubPullRequest(
            id = 2,
            repositoryId = repositoryId,
            page = 2,
            orderInPage = 0,
        )
        val pageOneSecondItem = Fixtures.createLocalGitHubPullRequest(
            id = 3,
            repositoryId = repositoryId,
            page = 1,
            orderInPage = 1,
        )
        val pageOneFirstItem = Fixtures.createLocalGitHubPullRequest(
            id = 1,
            repositoryId = repositoryId,
            page = 1,
            orderInPage = 0,
        )

        dao.savePullRequests(listOf(pageTwoItem, pageOneSecondItem, pageOneFirstItem))

        val result = loadPagedData(repositoryId)

        assertEquals(listOf(pageOneFirstItem, pageOneSecondItem, pageTwoItem), result)
    }

    @Test
    fun `countPullRequestsByRepositoryId should return only items from selected repository`() = runTest {
        val repo1Id = 1
        val repo2Id = 2
        dao.savePullRequests(
            listOf(
                Fixtures.createLocalGitHubPullRequest(id = 1L, repositoryId = repo1Id),
                Fixtures.createLocalGitHubPullRequest(id = 2L, repositoryId = repo1Id),
                Fixtures.createLocalGitHubPullRequest(id = 3L, repositoryId = repo2Id),
            ),
        )

        val result = dao.countPullRequestsByRepositoryId(repo1Id)

        assertEquals(2, result)
    }

    private suspend fun loadPagedData(repositoryId: Int): List<LocalGitHubPullRequest> {
        val pagingSource = dao.getPagedPullRequests(repositoryId)
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false,
            ),
        )

        assertTrue(loadResult is PagingSource.LoadResult.Page)
        return (loadResult as PagingSource.LoadResult.Page).data
    }
}
