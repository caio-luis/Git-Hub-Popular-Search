package com.caioluis.githubpopular.data.impl.remote.githubpulls.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.caioluis.githubpopular.core.common.exception.AppException
import com.caioluis.githubpopular.core.common.exception.ErrorMapper
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.githubpulls.mapper.LocalGitHubPullRequestMapperImpl
import com.caioluis.githubpopular.data.impl.local.githubpulls.source.GithubPullRequestsLocalSource
import com.caioluis.githubpopular.data.impl.remote.githubpulls.mapper.RemotePullRequestMapperImpl
import com.caioluis.githubpopular.data.impl.remote.githubpulls.source.PullRequestsRemoteSource
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
class GitHubPullRequestsRemoteMediatorTest {

    private val remoteSource: PullRequestsRemoteSource = mockk()
    private val localSource: GithubPullRequestsLocalSource = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk()

    private val remotePullRequestMapper = RemotePullRequestMapperImpl()
    private val localGitHubPullRequestMapper = LocalGitHubPullRequestMapperImpl()

    private lateinit var mediator: GitHubPullRequestsRemoteMediator

    private val repositoryId = Fixtures.REPOSITORY_ID
    private val pullUrl = "https://api.test.com/repos/user/repo/pulls"
    private val pagingState = PagingState<Int, LocalGitHubPullRequest>(
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

        mediator = GitHubPullRequestsRemoteMediator(
            pullUrl = pullUrl,
            repositoryId = repositoryId,
            remoteSource = remoteSource,
            localSource = localSource,
            errorMapper = errorMapper,
            remotePullRequestMapper = remotePullRequestMapper,
            localGitHubPullRequestMapper = localGitHubPullRequestMapper,
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

        coVerify(exactly = 0) { remoteSource.fetchPullRequests(any(), any()) }
    }

    @Test
    fun `load APPEND without remote key should return Success with endOfPaginationReached true`() = runTest {
        coEvery { localSource.getRemoteKey(repositoryId) } returns null

        val result = mediator.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { remoteSource.fetchPullRequests(any(), any()) }
    }

    @Test
    fun `load REFRESH successfully should replace cache and save remote key`() = runTest {
        val remotePullRequests = listOf(
            Fixtures.createRemotePullRequest(id = 10L),
            Fixtures.createRemotePullRequest(id = 11L),
        )

        coEvery {
            remoteSource.fetchPullRequests(
                pullUrl,
                Fixtures.STARTING_PAGE,
            )
        } returns remotePullRequests

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(!(result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        coVerify { localSource.deleteRemoteKey(repositoryId) }
        coVerify { localSource.deletePullRequestsByRepositoryId(repositoryId) }
        coVerify {
            localSource.insertRemoteKey(
                Fixtures.createPullRequestRemoteKey(repositoryId = repositoryId, nextPage = 2),
            )
        }
        coVerify {
            localSource.savePullRequests(
                listOf(
                    Fixtures.createLocalGitHubPullRequest(
                        id = 10L,
                        htmlUrl = "url",
                        title = "title",
                        body = "body",
                        userName = "user",
                        avatarUrl = "avatar",
                        repositoryId = repositoryId,
                        page = 1,
                        orderInPage = 0,
                    ),
                    Fixtures.createLocalGitHubPullRequest(
                        id = 11L,
                        htmlUrl = "url",
                        title = "title",
                        body = "body",
                        userName = "user",
                        avatarUrl = "avatar",
                        repositoryId = repositoryId,
                        page = 1,
                        orderInPage = 1,
                    ),
                ),
            )
        }
    }

    @Test
    fun `load REFRESH with cached data and network failure should keep cache`() = runTest {
        val exception = IOException("No internet")

        coEvery { remoteSource.fetchPullRequests(any(), any()) } throws exception
        coEvery { localSource.countPullRequestsByRepositoryId(repositoryId) } returns 2

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(!(result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { errorMapper.map(exception) }
    }

    @Test
    fun `load REFRESH without cached data should return mapped error`() = runTest {
        val exception = IOException("No internet")
        val mappedException = AppException.NetworkException(exception)

        coEvery { remoteSource.fetchPullRequests(any(), any()) } throws exception
        coEvery { localSource.countPullRequestsByRepositoryId(repositoryId) } returns 0
        every { errorMapper.map(exception) } returns mappedException

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertEquals(mappedException, (result as RemoteMediator.MediatorResult.Error).throwable)
    }

    @Test
    fun `load APPEND should return Error when HttpException is thrown`() = runTest {
        val exception = HttpException(Response.error<Any>(404, "".toResponseBody()))
        val mappedException = AppException.ServerException(exception)

        coEvery {
            localSource.getRemoteKey(repositoryId)
        } returns Fixtures.createPullRequestRemoteKey(repositoryId = repositoryId, nextPage = 2)
        coEvery { remoteSource.fetchPullRequests(pullUrl, 2) } throws exception
        every { errorMapper.map(exception) } returns mappedException

        val result = mediator.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertEquals(mappedException, (result as RemoteMediator.MediatorResult.Error).throwable)
    }
}
