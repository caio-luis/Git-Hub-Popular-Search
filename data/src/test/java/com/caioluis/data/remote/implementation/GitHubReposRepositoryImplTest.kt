package com.caioluis.data.remote.implementation

import com.caioluis.data.MainDispatcherRule
import com.caioluis.data.local.LocalSource
import com.caioluis.data.remote.implementation.Fixtures.domainGitHubRepositories
import com.caioluis.data.remote.implementation.Fixtures.remoteGitHubRepositories
import com.caioluis.domain.repository.GitHubReposRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GitHubReposRepositoryImplTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var remoteSource: RemoteSource
    private lateinit var localSource: LocalSource

    private lateinit var repositoryImpl: GitHubReposRepository

    @Before
    fun setUp() {
        remoteSource = mockk(relaxed = true)
        localSource = mockk(relaxed = true)
        repositoryImpl = GitHubReposRepositoryImpl(remoteSource, localSource)
    }

    @Test
    fun `when remote source succeeds should return data`() = runBlocking {
        val page = 1
        val language = "kotlin"

        coEvery {
            remoteSource.fetchFromRemote(
                any(),
                any()
            )
        } returns remoteGitHubRepositories.repositories

        coEvery {
            localSource.getFromLocalDatabase(any(), any())
        } returns domainGitHubRepositories

        val result = repositoryImpl.getGitHubRepositories(page, language)

        coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, language) }
        coVerify(exactly = 1) {
            localSource.getFromLocalDatabase(any(), any())
        }

        assertNotNull(result)
        assertEquals(domainGitHubRepositories, result)
    }

    @Test
    fun `when remote source fails and local source has data, should get from local source`() =
        runBlocking {
            val page = 1
            val language = "kotlin"

            coEvery {
                remoteSource.fetchFromRemote(any(), any())
            } returns remoteGitHubRepositories.repositories

            coEvery {
                localSource.getFromLocalDatabase(any(), any())
            } returns domainGitHubRepositories

            val result = repositoryImpl.getGitHubRepositories(page, language)

            coVerify(exactly = 1) {
                localSource.getFromLocalDatabase(any(), any())
            }

            coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, language) }

            assertNotNull(result)
            assertEquals(domainGitHubRepositories, result)
        }

    @Test(expected = Exception::class)
    fun `when remote source fails and local source is empty, should throw exception`() =
        runBlocking {
            val page = 1
            val language = "kotlin"

            coEvery { remoteSource.fetchFromRemote(any(), any()) } throws Exception()
            coEvery { localSource.getFromLocalDatabase(any(), any()) } returns null

            repositoryImpl.getGitHubRepositories(page, language)

            coVerify(exactly = 1) {
                localSource.getFromLocalDatabase(any(), any())
            }

            coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, language) }
        }
}
