package com.caioluis.githubpopular.data.impl.remote

import android.util.Log
import com.caioluis.githubpopular.core.common.exception.AppException
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.core.common.utils.LogUtil
import com.caioluis.githubpopular.data.bridge.remote.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.model.RemoteRepositoryOwner
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

class GitHubReposRepositoryImplTest {

    private val remoteSource = mockk<RemoteSource>()
    private val localSource = mockk<LocalSource>()
    private val errorMapper = mockk<ErrorMapper>()
    private lateinit var repository: GitHubReposRepositoryImpl

    @Before
    fun setUp() {
        repository = GitHubReposRepositoryImpl(remoteSource, localSource, errorMapper)

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
        val exception = Exception("Remote error")
        val appException = AppException.NetworkException()

        coEvery { remoteSource.fetchFromRemote(page, language) } throws exception
        coEvery { localSource.getFromCache(page, language) } returns expected
        every { errorMapper.map(exception) } returns appException

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertEquals(expected, result)
        coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, language) }
        coVerify(exactly = 1) { localSource.getFromCache(page, language) }
    }

    @Test(expected = AppException.NetworkException::class)
    fun `getGitHubRepositories throws exception when both remote and local fail`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val exception = Exception("Remote error")
        val appException = AppException.NetworkException(cause = exception)

        coEvery { remoteSource.fetchFromRemote(page, language) } throws exception
        coEvery { localSource.getFromCache(page, language) } returns emptyList()
        every { errorMapper.map(exception) } returns appException

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
        coVerify(exactly = 0) { localSource.saveToLocalCache(any(), any(), any()) }
        coVerify(exactly = 0) { localSource.getFromCache(any(), any()) }
    }

    @Test
    fun `getGitHubRepositories returns empty list when remote returns list with null items`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"

        coEvery { remoteSource.fetchFromRemote(page, language) } returns listOf(null, null)

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, language) }
        coVerify(exactly = 0) { localSource.saveToLocalCache(any(), any(), any()) }
    }

    @Test
    fun `getGitHubRepositories handles partial null items in remote list`() = runTest {
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
        val remoteList = listOf(remoteRepo, null, remoteRepo)

        coEvery { remoteSource.fetchFromRemote(page, language) } returns remoteList
        coEvery { localSource.saveToLocalCache(any(), page, language) } returns Unit

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertEquals(2, result.size)
        coVerify(exactly = 1) { localSource.saveToLocalCache(any(), page, language) }
    }

    @Test
    fun `getGitHubRepositories returns data from multiple pages`() = runTest {
        // Arrange
        val page1 = 1
        val page2 = 2
        val language = "Kotlin"
        val remoteRepo1 = RemoteGitHubRepository(
            id = 1,
            name = "Test Repo 1",
            description = "Description 1",
            stargazersCount = 100,
            forksCount = 10,
            htmlUrl = "url1",
            pullsUrl = "pulls_url1",
            owner = RemoteRepositoryOwner(
                login = "user1",
                avatarUrl = "avatar1",
            ),
        )
        val remoteRepo2 = RemoteGitHubRepository(
            id = 2,
            name = "Test Repo 2",
            description = "Description 2",
            stargazersCount = 200,
            forksCount = 20,
            htmlUrl = "url2",
            pullsUrl = "pulls_url2",
            owner = RemoteRepositoryOwner(
                login = "user2",
                avatarUrl = "avatar2",
            ),
        )

        coEvery { remoteSource.fetchFromRemote(page1, language) } returns listOf(remoteRepo1)
        coEvery { remoteSource.fetchFromRemote(page2, language) } returns listOf(remoteRepo2)
        coEvery { localSource.saveToLocalCache(any(), any(), language) } returns Unit

        // Act
        val result1 = repository.getGitHubRepositories(page1, language)
        val result2 = repository.getGitHubRepositories(page2, language)

        // Assert
        assertEquals(1, result1.size)
        assertEquals(1, result2.size)
        assertEquals(1, result1.first().id)
        assertEquals(2, result2.first().id)
    }

    @Test
    fun `getGitHubRepositories with different languages`() = runTest {
        // Arrange
        val page = 1
        val languageKotlin = "Kotlin"
        val languageJava = "Java"
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

        coEvery { remoteSource.fetchFromRemote(page, languageKotlin) } returns listOf(remoteRepo)
        coEvery { remoteSource.fetchFromRemote(page, languageJava) } returns listOf(remoteRepo)
        coEvery { localSource.saveToLocalCache(any(), page, any()) } returns Unit

        // Act
        val resultKotlin = repository.getGitHubRepositories(page, languageKotlin)
        val resultJava = repository.getGitHubRepositories(page, languageJava)

        // Assert
        assertEquals(1, resultKotlin.size)
        assertEquals(1, resultJava.size)
        coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, languageKotlin) }
        coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, languageJava) }
    }

    @Test
    fun `getGitHubRepositories with multiple items`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val remoteRepos = (1..5).map { id ->
            RemoteGitHubRepository(
                id = id,
                name = "Test Repo $id",
                description = "Description $id",
                stargazersCount = 100 * id,
                forksCount = 10 * id,
                htmlUrl = "url$id",
                pullsUrl = "pulls_url$id",
                owner = RemoteRepositoryOwner(
                    login = "user$id",
                    avatarUrl = "avatar$id",
                ),
            )
        }

        coEvery { remoteSource.fetchFromRemote(page, language) } returns remoteRepos
        coEvery { localSource.saveToLocalCache(any(), page, language) } returns Unit

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertEquals(5, result.size)
        coVerify(exactly = 1) { localSource.saveToLocalCache(any(), page, language) }
    }

    @Test
    fun `getGitHubRepositories handles NetworkException from remote`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val exception = Exception("Network error")
        val appException = AppException.NetworkException(cause = exception)
        val cachedData = listOf(DomainGitHubRepository(id = 1))

        coEvery { remoteSource.fetchFromRemote(page, language) } throws exception
        coEvery { localSource.getFromCache(page, language) } returns cachedData
        every { errorMapper.map(exception) } returns appException

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertEquals(cachedData, result)
        coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, language) }
        coVerify(exactly = 1) { localSource.getFromCache(page, language) }
    }

    @Test
    fun `getGitHubRepositories handles ServerException from remote`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val exception = Exception("Server error")
        val appException = AppException.ServerException(cause = exception)
        val cachedData = listOf(DomainGitHubRepository(id = 1), DomainGitHubRepository(id = 2))

        coEvery { remoteSource.fetchFromRemote(page, language) } throws exception
        coEvery { localSource.getFromCache(page, language) } returns cachedData
        every { errorMapper.map(exception) } returns appException

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertEquals(2, result.size)
        assertEquals(cachedData, result)
    }

    @Test
    fun `getGitHubRepositories does not call saveToLocalCache when result is empty after filter`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"

        coEvery { remoteSource.fetchFromRemote(page, language) } returns null

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertTrue(result.isEmpty())
        coVerify(exactly = 0) { localSource.saveToLocalCache(any(), any(), any()) }
    }

    @Test
    fun `getGitHubRepositories returns correct data structure`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val remoteRepo = RemoteGitHubRepository(
            id = 123,
            name = "Test Repo",
            description = "Description",
            stargazersCount = 500,
            forksCount = 50,
            htmlUrl = "https://github.com/test/repo",
            pullsUrl = "https://api.github.com/repos/test/repo/pulls",
            owner = RemoteRepositoryOwner(
                login = "testuser",
                avatarUrl = "https://avatars.githubusercontent.com/u/1",
            ),
        )

        coEvery { remoteSource.fetchFromRemote(page, language) } returns listOf(remoteRepo)
        coEvery { localSource.saveToLocalCache(any(), page, language) } returns Unit

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertEquals(1, result.size)
        val domainRepo = result.first()
        assertEquals(123, domainRepo.id)
        assertNotNull(domainRepo)
    }

    @Test(expected = AppException.NetworkException::class)
    fun `getGitHubRepositories verifies error mapping is called on exception`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val originalException = IOException("Network error")
        val appException = AppException.NetworkException(cause = originalException)

        coEvery { remoteSource.fetchFromRemote(page, language) } throws originalException
        coEvery { localSource.getFromCache(page, language) } returns emptyList()
        every { errorMapper.map(originalException) } returns appException

        // Act
        repository.getGitHubRepositories(page, language)

        // Assert
        coVerify(exactly = 1) { errorMapper.map(originalException) }
    }

    @Test
    fun `getGitHubRepositories preserves cause chain in exception`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val originalCause = Exception("Original error")
        val appException = AppException.ServerException(cause = originalCause)

        coEvery { remoteSource.fetchFromRemote(page, language) } throws Exception("Error")
        coEvery { localSource.getFromCache(page, language) } returns emptyList()
        every { errorMapper.map(any()) } returns appException

        // Act & Assert
        try {
            repository.getGitHubRepositories(page, language)
            assert(false) { "Should have thrown an exception" }
        } catch (e: AppException.ServerException) {
            assertEquals(originalCause, e.cause)
        }
    }

    @Test
    fun `getGitHubRepositories uses local cache first on error`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val cachedData = listOf(
            DomainGitHubRepository(id = 1),
            DomainGitHubRepository(id = 2),
            DomainGitHubRepository(id = 3),
        )

        coEvery { remoteSource.fetchFromRemote(page, language) } throws Exception("Error")
        coEvery { localSource.getFromCache(page, language) } returns cachedData
        every { errorMapper.map(any()) } returns AppException.NetworkException()

        // Act
        val result = repository.getGitHubRepositories(page, language)

        // Assert
        assertEquals(3, result.size)
        assertEquals(cachedData, result)
        coVerify(exactly = 1) { remoteSource.fetchFromRemote(page, language) }
        coVerify(exactly = 1) { localSource.getFromCache(page, language) }
    }

    @Test
    fun `getGitHubRepositories saves correct data to local cache`() = runTest {
        // Arrange
        val page = 2
        val language = "Python"
        val remoteRepos = (1..3).map { id ->
            RemoteGitHubRepository(
                id = id,
                name = "Repo $id",
                description = "Description $id",
                stargazersCount = 100 * id,
                forksCount = 10 * id,
                htmlUrl = "url$id",
                pullsUrl = "pulls_url$id",
                owner = RemoteRepositoryOwner(
                    login = "user$id",
                    avatarUrl = "avatar$id",
                ),
            )
        }

        coEvery { remoteSource.fetchFromRemote(page, language) } returns remoteRepos
        coEvery { localSource.saveToLocalCache(any(), page, language) } returns Unit

        // Act
        repository.getGitHubRepositories(page, language)

        // Assert
        coVerify(exactly = 1) { localSource.saveToLocalCache(any(), page, language) }
    }

    @Test(expected = AppException.TimeoutException::class)
    fun `getGitHubRepositories propagates TimeoutException when local cache is empty`() = runTest {
        // Arrange
        val page = 1
        val language = "Kotlin"
        val exception = SocketTimeoutException("Timeout")
        val appException = AppException.TimeoutException(cause = exception)

        coEvery { remoteSource.fetchFromRemote(page, language) } throws exception
        coEvery { localSource.getFromCache(page, language) } returns emptyList()
        every { errorMapper.map(exception) } returns appException

        // Act
        repository.getGitHubRepositories(page, language)
    }
}
