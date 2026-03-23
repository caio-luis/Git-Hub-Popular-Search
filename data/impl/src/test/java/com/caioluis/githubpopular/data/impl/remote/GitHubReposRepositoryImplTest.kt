package com.caioluis.githubpopular.data.impl.remote

import android.util.Log
import com.caioluis.githubpopular.core.common.utils.LogUtil
import com.caioluis.githubpopular.data.bridge.remote.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.model.RemoteRepositoryOwner
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.githubrepos.LocalSource
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GitHubReposRepositoryImplTest {

    private val remoteSource = mockk<RemoteSource>()
    private val localSource = mockk<LocalSource>()
    private lateinit var repository: GitHubReposRepositoryImpl

    @Before
    fun setUp() {
        repository = GitHubReposRepositoryImpl(remoteSource, localSource)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.w(any(), any<String>(), any()) } returns 0

        LogUtil.isDebug = true
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `getGitHubRepositories returns data from remote and saves to local`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val remoteRepo = RemoteGitHubRepository(
            id = 1,
            name = "Test Repo",
            description = "Description",
            stargazersCount = 100,
            forksCount = 10,
            htmlUrl = "url",
            pullsUrl = "pulls_url",
            owner = RemoteRepositoryOwner(
                login = "user",
                avatarUrl = "avatar",
            ),
        )
        val remoteList = listOf(remoteRepo)

        coEvery { remoteSource.fetchFromRemote(page, language) } returns remoteList
        coEvery { localSource.saveToLocalCache(any(), page, language) } returns Unit

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertEquals(remoteList.size, result.size)
        assertEquals(remoteRepo.id, result.first().id)
        coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, language) }
        coVerify(exactly = 1) { localSource.saveToLocalCache(any(), page, language) }
    }

    @Test
    fun `getGitHubRepositories returns data from local when remote fails`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val expected = listOf(DomainGitHubRepository(id = 1))

        coEvery { remoteSource.fetchFromRemote(page, language) } throws Exception("Remote error")
        coEvery { localSource.getFromCache(page, language) } returns expected

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertEquals(expected, result)
        coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, language) }
        coVerify(exactly = 1) { localSource.getFromCache(page, language) }
    }

    @Test(expected = Exception::class)
    fun `getGitHubRepositories throws exception when both remote and local fail`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val exception = Exception("Remote error")

        coEvery { remoteSource.fetchFromRemote(page, language) } throws exception
        coEvery { localSource.getFromCache(page, language) } returns emptyList()

        // Act
        repository.getGitHubRepositories(page, language)
    }

    @Test
    fun `getGitHubRepositories returns empty list when remote returns empty list`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"

        coEvery { remoteSource.fetchFromRemote(page, language) } returns emptyList()

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, language) }
        coVerify(exactly = 0) { localSource.getFromCache(any(), any()) }
    }
}
