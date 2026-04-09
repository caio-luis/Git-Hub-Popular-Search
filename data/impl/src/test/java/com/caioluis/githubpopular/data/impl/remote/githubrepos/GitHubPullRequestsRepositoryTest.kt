package com.caioluis.githubpopular.data.impl.remote.githubrepos

import androidx.paging.ExperimentalPagingApi
import com.caioluis.githubpopular.data.bridge.mappers.LocalGitHubPullRequestMapper
import com.caioluis.githubpopular.data.impl.Fixtures
import com.caioluis.githubpopular.data.impl.local.GitHubReposDataBase
import com.caioluis.githubpopular.data.impl.remote.githubpulls.GitHubPullRequestsRemoteMediator
import com.caioluis.githubpopular.data.impl.remote.githubpulls.GithubPullRequestsRemoteMediatorFactory
import com.caioluis.githubpopular.data.impl.remote.githubpulls.repository.GitHubPullRequestsRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class GitHubPullRequestsRepositoryTest {

    private val localDatabase = mockk<GitHubReposDataBase>()
    private val remoteMediatorFactory = mockk<GithubPullRequestsRemoteMediatorFactory>()
    private val localGitHubPullRequestMapper = mockk<LocalGitHubPullRequestMapper>()
    private val remoteMediator = mockk<GitHubPullRequestsRemoteMediator>()

    private lateinit var repository: GitHubPullRequestsRepositoryImpl

    @Before
    fun setUp() {
        repository = GitHubPullRequestsRepositoryImpl(
            localDatabase = localDatabase,
            remoteMediatorFactory = remoteMediatorFactory,
            localGitHubPullRequestMapper = localGitHubPullRequestMapper,
        )
    }

    @Test
    fun `getPullRequests should create paged flow using remote mediator factory`() {
        val pullUrl = "http://pull-url"
        val repositoryId = Fixtures.REPOSITORY_ID

        every {
            remoteMediatorFactory.create(
                pullUrl = pullUrl,
                repositoryId = repositoryId,
            )
        } returns remoteMediator
        every { localDatabase.gitHubPullRequestsDao() } returns mockk(relaxed = true)

        val result = repository.getPullRequests(pullUrl, repositoryId)

        assertNotNull(result)

        verify(exactly = 1) {
            remoteMediatorFactory.create(
                pullUrl = pullUrl,
                repositoryId = repositoryId,
            )
        }
    }
}
