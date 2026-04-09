package com.caioluis.githubpopular.data.impl.remote.githubrepos

import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.remote.githubrepos.service.GitHubRepositoriesService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GithubReposRemoteSourceTest {

    private val gitHubRepositoriesService: GitHubRepositoriesService = mockk()

    private lateinit var remoteSource: GithubReposRemoteSourceImpl

    @Before
    fun setup() {
        remoteSource = GithubReposRemoteSourceImpl(gitHubRepositoriesService)
    }

    @Test
    fun `fetchFromRemote should return list of repositories when service returns valid data`() = runTest {
        val expectedList = listOf(Fixtures.createRemoteGitHubRepository())
        val mockResponse =
            Fixtures.createRemoteGitHubRepositoriesResponse(repositories = expectedList)

        coEvery {
            gitHubRepositoriesService.getGitHubRepositories(
                language = any(),
                sort = any(),
                page = Fixtures.STARTING_PAGE,
            )
        } returns mockResponse

        val result =
            remoteSource.fetchFromRemote(Fixtures.STARTING_PAGE, Fixtures.DEFAULT_LANGUAGE)

        assertEquals(expectedList, result)
        coVerify(exactly = 1) {
            gitHubRepositoriesService.getGitHubRepositories(
                language = any(),
                sort = any(),
                page = Fixtures.STARTING_PAGE,
            )
        }
    }

    @Test
    fun `fetchFromRemote should return empty list when service returns empty repositories list`() = runTest {
        val expectedList = emptyList<RemoteGitHubRepository>()
        val mockResponse =
            Fixtures.createRemoteGitHubRepositoriesResponse(repositories = expectedList)

        coEvery {
            gitHubRepositoriesService.getGitHubRepositories(
                language = any(),
                sort = any(),
                page = Fixtures.STARTING_PAGE,
            )
        } returns mockResponse

        val result =
            remoteSource.fetchFromRemote(Fixtures.STARTING_PAGE, Fixtures.DEFAULT_LANGUAGE)

        assertEquals(expectedList, result)
        coVerify(exactly = 1) {
            gitHubRepositoriesService.getGitHubRepositories(
                language = any(),
                sort = any(),
                page = Fixtures.STARTING_PAGE,
            )
        }
    }

    @Test
    fun `fetchFromRemote should return null when service returns null`() = runTest {
        coEvery {
            gitHubRepositoriesService.getGitHubRepositories(
                language = any(),
                sort = any(),
                page = Fixtures.STARTING_PAGE,
            )
        } returns null

        val result = remoteSource.fetchFromRemote(Fixtures.STARTING_PAGE, Fixtures.DEFAULT_LANGUAGE)

        assertNull(result)
        coVerify(exactly = 1) {
            gitHubRepositoriesService.getGitHubRepositories(
                language = any(),
                sort = any(),
                page = Fixtures.STARTING_PAGE,
            )
        }
    }

    @Test(expected = Exception::class)
    fun `fetchFromRemote should throw exception when service throws exception`() = runTest {
        coEvery {
            gitHubRepositoriesService.getGitHubRepositories(
                language = any(),
                sort = any(),
                page = Fixtures.STARTING_PAGE,
            )
        } throws Exception("Network error")

        remoteSource.fetchFromRemote(Fixtures.STARTING_PAGE, Fixtures.DEFAULT_LANGUAGE)
    }
}
