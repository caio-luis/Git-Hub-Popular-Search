package com.caioluis.data.remote.implementation

import com.caioluis.data.local.LocalSource
import com.caioluis.data.remote.implementation.Fixtures.domainGitHubRepositories
import com.caioluis.data.remote.implementation.Fixtures.language
import com.caioluis.data.remote.implementation.Fixtures.page
import com.caioluis.data.remote.implementation.Fixtures.remoteGitHubRepositories
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GitHubReposRepositoryImplTest {

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
    fun `when remote source succeeds should return data`(): Unit = runTest {
        coEvery {
            remoteSource.fetchFromRemote(any(), any())
        } returns remoteGitHubRepositories.repositories

        coEvery {
            localSource.saveToLocalCache(any(), any(), any())
        } returns Unit

        coEvery {
            localSource.getFromCache(any(), any())
        } returns domainGitHubRepositories

        repositoryImpl
            .getGitHubRepositories(page, language)
            .single()
            .apply {
                assertNotNull(this)
                assertEquals(domainGitHubRepositories, this)
            }
    }

    @Test
    fun `when remote source fails and local source has data, should get from local source`(): Unit =
        runTest {
            coEvery { remoteSource.fetchFromRemote(any(), any()) } throws Exception("Remote failed")
            coEvery { localSource.getFromCache(any(), any()) } returns domainGitHubRepositories

            repositoryImpl
                .getGitHubRepositories(page, language)
                .single().apply {
                    assertNotNull(this)
                    assertEquals(domainGitHubRepositories, this)
                }

            coVerify(exactly = 0) { localSource.saveToLocalCache(any(), page, language) }
        }

    @Test
    fun `when remote source fails and local source is empty, should throw exception`(): Unit =
        runTest {
            coEvery { remoteSource.fetchFromRemote(any(), any()) } throws Exception("Remote failed")
            coEvery { localSource.getFromCache(any(), any()) } returns null

            val result = repositoryImpl.getGitHubRepositories(page, language)
            assertFails { result.single() }
        }
}
