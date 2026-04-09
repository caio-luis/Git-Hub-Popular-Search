package com.caioluis.githubpopular.data.impl.remote.githubrepos

import androidx.paging.ExperimentalPagingApi
import com.caioluis.githubpopular.data.impl.local.githubrepos.GithubReposLocalSource
import com.caioluis.githubpopular.data.impl.mapper.LocalGitHubRepositoryMapper
import com.caioluis.githubpopular.data.impl.remote.githubrepos.repository.GitHubReposRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class GitHubReposRepositoryImplTest {

    private val localSource = mockk<GithubReposLocalSource>(relaxed = true)
    private val remoteMediatorFactory = mockk<GithubReposRemoteMediatorFactory>()
    private val localGitHubRepositoryMapper = mockk<LocalGitHubRepositoryMapper>()
    private val remoteMediator = mockk<GitHubReposRemoteMediator>()

    private lateinit var repository: GitHubReposRepositoryImpl

    @Before
    fun setup() {
        repository = GitHubReposRepositoryImpl(
            localSource = localSource,
            remoteMediatorFactory = remoteMediatorFactory,
            localGitHubRepositoryMapper = localGitHubRepositoryMapper,
        )
    }

    @Test
    fun `getGitHubRepositories should create paged flow using remote mediator factory`() {
        val language = "Kotlin"

        every { remoteMediatorFactory.create(language) } returns remoteMediator

        val result = repository.getGitHubRepositories(language)

        assertNotNull(result)
        verify(exactly = 1) { remoteMediatorFactory.create(language) }
    }
}
