package com.caioluis.data.remote.implementation

import com.caioluis.data.MainDispatcherRule
import com.caioluis.data.local.GitHubRepositoriesDao
import com.caioluis.data.remote.model.RemoteGitHubRepositories
import com.caioluis.data.remote.service.GitHubRepositoriesService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GitHubRepositoriesImplTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var gitHubRepositoriesImpl: GitHubRepositoriesImpl
    private lateinit var remoteService: GitHubRepositoriesService
    private lateinit var localDao: GitHubRepositoriesDao

    @Before
    fun setUp() {
        remoteService = mockk(relaxed = true)
        localDao = mockk(relaxed = true)

        gitHubRepositoriesImpl = GitHubRepositoriesImpl(
            gitHubRepositoriesService = remoteService,
            gitHubRepositoriesDao = localDao
        )
    }

    @Test
    fun `when remote request is successful, save it locally and return from remote`() =
        runBlocking {
            // Given
            remoteRequestMock()

            // When
            val result = gitHubRepositoriesImpl.getGitHubRepositories(page = 1)

            // Then
            assertNotNull(result)
            assertEquals(Fixtures.remoteGitHubRepositories.repositories?.size, result.size)

            coVerify(exactly = 1) { localDao.saveRepositories(any()) }
            coVerify(exactly = 0) { localDao.getAllRepositories(any()) }
        }

    @Test
    fun `when remote request fails and local cache is empty, should throw exception`() =
        runBlocking {
            // Given
            remoteRequestMock(throwsException = true)
            localRequestMock(emptyResult = true)

            // When
            assertFailsWith<Exception> { gitHubRepositoriesImpl.getGitHubRepositories(page = 1) }

            // Then
            coVerify(exactly = 0) { localDao.saveRepositories(any()) }
        }

    @Test
    fun `when remote request fails and local cache is not empty, should get from cache`() =
        runBlocking {
            // Given
            remoteRequestMock(throwsException = true)
            localRequestMock()

            // When
            val result = gitHubRepositoriesImpl.getGitHubRepositories(page = 1)

            // Then
            assertNotNull(result)
            assertEquals(Fixtures.localGitHubRepositories.size, result.size)
            coVerify(exactly = 0) { localDao.saveRepositories(any()) }
        }

    @Test
    fun `when remote is empty and local cache is not empty, should get from cache`() =
        runBlocking {
            // Given
            remoteRequestMock(emptyResponse = true)
            localRequestMock()

            // When
            val result = gitHubRepositoriesImpl.getGitHubRepositories(page = 1)

            // Then
            assertNotNull(result)
            assertEquals(Fixtures.localGitHubRepositories.size, result.size)
            coVerify(exactly = 0) { localDao.saveRepositories(any()) }
        }

    @Test
    fun `when page is 1, should delete previous local cache before saving new repositories`() =
        runBlocking {
            // Given
            remoteRequestMock()
            localRequestMock()

            // When
            gitHubRepositoriesImpl.getGitHubRepositories(page = 1)

            // Then
            coVerifyOrder {
                localDao.deleteAllGitHubRepositories()
                localDao.saveRepositories(any())
            }
        }

    @Test
    fun `when page is greater than 1, should not delete previous local cache before saving new repositories`() =
        runBlocking {
            // Given
            remoteRequestMock()
            localRequestMock()

            // When
            gitHubRepositoriesImpl.getGitHubRepositories(page = 2)

            // Then
            coVerify { localDao.saveRepositories(any()) }
            coVerify(exactly = 0) { localDao.deleteAllGitHubRepositories() }
        }


    private fun remoteRequestMock(
        throwsException: Boolean = false,
        emptyResponse: Boolean = false,
    ) {
        when {
            throwsException -> {
                coEvery {
                    remoteService.getGitHubRepositories(page = any())
                } throws Exception("Remote server error")
            }
            emptyResponse -> {
                coEvery {
                    remoteService.getGitHubRepositories(page = any())
                } returns RemoteGitHubRepositories(repositories = emptyList())
            }
            else -> {
                coEvery {
                    remoteService.getGitHubRepositories(page = any())
                } returns Fixtures.remoteGitHubRepositories
            }
        }
    }

    private fun localRequestMock(emptyResult: Boolean = false) {
        if (emptyResult)
            coEvery { localDao.getAllRepositories(any()) } returns emptyList()
        else
            coEvery {
                localDao.getAllRepositories(any())
            } returns Fixtures.localGitHubRepositories
    }
}
