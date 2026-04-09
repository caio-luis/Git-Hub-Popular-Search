package com.caioluis.githubpopular.data.impl.remote.githubrepos

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.caioluis.githubpopular.core.common.exception.AppException
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.githubrepos.GithubReposLocalSource
import com.caioluis.githubpopular.data.impl.mapper.LocalGitHubRepositoryMapperImpl
import com.caioluis.githubpopular.data.impl.mapper.RemoteGitHubRepositoryMapperImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class GitHubReposRemoteMediatorTest {

    private val remoteSource: GithubReposRemoteSource = mockk()
    private val localSource: GithubReposLocalSource = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk()

    private val remoteGitHubRepositoryMapper = RemoteGitHubRepositoryMapperImpl()
    private val localGitHubRepositoryMapper = LocalGitHubRepositoryMapperImpl()

    private lateinit var mediator: GitHubReposRemoteMediator

    private val pagingState = PagingState<Int, LocalGitHubRepository>(
        pages = emptyList(),
        anchorPosition = null,
        config = PagingConfig(pageSize = 20),
        leadingPlaceholderCount = 0,
    )

    @Before
    fun setup() {
        coEvery { localSource.withTransaction(any<suspend () -> Any>()) } coAnswers {
            firstArg<suspend () -> Any>().invoke()
        }

        mediator = GitHubReposRemoteMediator(
            language = Fixtures.DEFAULT_LANGUAGE,
            remoteSource = remoteSource,
            localSource = localSource,
            errorMapper = errorMapper,
            remoteGitHubRepositoryMapper = remoteGitHubRepositoryMapper,
            localGitHubRepositoryMapper = localGitHubRepositoryMapper,
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `load PREPEND should return Success with endOfPaginationReached true`() = runTest {
        val result = mediator.load(LoadType.PREPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        coVerify(exactly = 0) { remoteSource.fetchFromRemote(any(), any()) }
    }

    @Test
    fun `load APPEND without remote key should return Success with endOfPaginationReached true`() = runTest {
        coEvery { localSource.getRemoteKey(Fixtures.DEFAULT_LANGUAGE) } returns null

        val result = mediator.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { remoteSource.fetchFromRemote(any(), any()) }
    }

    @Test
    fun `load APPEND with remote key should request next page`() = runTest {
        val nextPage = 2
        val remoteKey = Fixtures.createRemoteKey(nextPage = nextPage)

        coEvery { localSource.getRemoteKey(Fixtures.DEFAULT_LANGUAGE) } returns remoteKey
        coEvery {
            remoteSource.fetchFromRemote(
                nextPage,
                Fixtures.DEFAULT_LANGUAGE,
            )
        } returns emptyList()

        val result = mediator.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        coVerify(exactly = 1) { remoteSource.fetchFromRemote(nextPage, Fixtures.DEFAULT_LANGUAGE) }
        coVerify { localSource.insertRemoteKey(Fixtures.createRemoteKey(nextPage = null)) }
    }

    @Test
    fun `load APPEND with data should keep pagination open and persist mapped entities`() = runTest {
        val nextPage = 2
        val remoteKey = Fixtures.createRemoteKey(nextPage = nextPage)
        val remoteRepository = Fixtures.createRemoteGitHubRepository(id = 7)

        coEvery { localSource.getRemoteKey(Fixtures.DEFAULT_LANGUAGE) } returns remoteKey
        coEvery {
            remoteSource.fetchFromRemote(nextPage, Fixtures.DEFAULT_LANGUAGE)
        } returns listOf(remoteRepository)

        val result = mediator.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(!(result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        coVerify { localSource.insertRemoteKey(Fixtures.createRemoteKey(nextPage = 3)) }
        coVerify {
            localSource.saveRepositories(
                listOf(
                    LocalGitHubRepository(
                        id = 7,
                        title = "Repo 7",
                        description = "Description",
                        pullsUrl = "https://api.github.com/repos/user/repo7/pulls",
                        stargazersCount = 100,
                        forksCount = 10,
                        repositoryUrl = "https://github.com/user/repo7",
                        page = nextPage,
                        language = Fixtures.DEFAULT_LANGUAGE,
                        userName = "user",
                        avatarUrl = "avatar",
                    ),
                ),
            )
        }
    }

    @Test
    fun `load REFRESH successfully should clear database and save new data`() = runTest {
        coEvery {
            remoteSource.fetchFromRemote(
                Fixtures.STARTING_PAGE,
                Fixtures.DEFAULT_LANGUAGE,
            )
        } returns emptyList()

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        coVerify { localSource.deleteRemoteKey(Fixtures.DEFAULT_LANGUAGE) }
        coVerify { localSource.clearRepositories(Fixtures.DEFAULT_LANGUAGE) }
        coVerify { localSource.insertRemoteKey(Fixtures.createRemoteKey(nextPage = null)) }
        coVerify { localSource.saveRepositories(emptyList()) }
    }

    @Test
    fun `load REFRESH with data should save mapped entities and set next page`() = runTest {
        val remoteRepository = Fixtures.createRemoteGitHubRepository(id = 21)

        coEvery {
            remoteSource.fetchFromRemote(
                Fixtures.STARTING_PAGE,
                Fixtures.DEFAULT_LANGUAGE,
            )
        } returns listOf(remoteRepository)

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(!(result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        coVerify { localSource.insertRemoteKey(Fixtures.createRemoteKey(nextPage = 2)) }
        coVerify {
            localSource.saveRepositories(
                listOf(
                    LocalGitHubRepository(
                        id = 21,
                        title = "Repo 21",
                        description = "Description",
                        pullsUrl = "https://api.github.com/repos/user/repo21/pulls",
                        stargazersCount = 100,
                        forksCount = 10,
                        repositoryUrl = "https://github.com/user/repo21",
                        page = Fixtures.STARTING_PAGE,
                        language = Fixtures.DEFAULT_LANGUAGE,
                        userName = "user",
                        avatarUrl = "avatar",
                    ),
                ),
            )
        }
    }

    @Test
    fun `load should return Error when IOException is thrown`() = runTest {
        val exception = IOException("No internet")
        val mappedException = AppException.NetworkException(exception)

        coEvery { remoteSource.fetchFromRemote(any(), any()) } throws exception
        every { errorMapper.map(exception) } returns mappedException

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertEquals(mappedException, (result as RemoteMediator.MediatorResult.Error).throwable)
    }

    @Test
    fun `load should return Error when HttpException is thrown`() = runTest {
        val response = Response.error<Any>(404, "".toResponseBody())
        val exception = HttpException(response)
        val mappedException = AppException.ServerException(exception)

        coEvery { remoteSource.fetchFromRemote(any(), any()) } throws exception
        every { errorMapper.map(exception) } returns mappedException

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertEquals(mappedException, (result as RemoteMediator.MediatorResult.Error).throwable)
    }

    @Test
    fun `load should return Error when generic Exception is thrown`() = runTest {
        val exception = Exception("Unknown error")
        val mappedException = AppException.UnknownException(exception)

        coEvery { remoteSource.fetchFromRemote(any(), any()) } throws exception
        every { errorMapper.map(exception) } returns mappedException

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertEquals(mappedException, (result as RemoteMediator.MediatorResult.Error).throwable)
    }
}
