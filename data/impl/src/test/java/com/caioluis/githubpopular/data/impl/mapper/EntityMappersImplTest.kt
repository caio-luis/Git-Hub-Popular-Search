package com.caioluis.githubpopular.data.impl.mapper

import com.caioluis.githubpopular.data.impl.Fixtures
import org.junit.Assert.assertEquals
import org.junit.Test

class EntityMappersImplTest {

    private val remoteGitHubRepositoryMapper = RemoteGitHubRepositoryMapperImpl()
    private val remotePullRequestMapper = RemotePullRequestMapperImpl()
    private val localGitHubRepositoryMapper = LocalGitHubRepositoryMapperImpl()
    private val localGitHubPullRequestMapper = LocalGitHubPullRequestMapperImpl()

    @Test
    fun `remote repository mapper should map nullable fields to defaults`() {
        val mapped = remoteGitHubRepositoryMapper.mapToDomain(
            remoteRepository = Fixtures.createRemoteGitHubRepository().copy(
                id = null,
                name = null,
                description = null,
                pullsUrl = null,
                stargazersCount = null,
                forksCount = null,
                htmlUrl = null,
                owner = null,
            ),
            page = 2,
            language = "Java",
        )

        assertEquals(-1, mapped.id)
        assertEquals("", mapped.title)
        assertEquals("", mapped.description)
        assertEquals("", mapped.pullsUrl)
        assertEquals(0, mapped.stargazersCount)
        assertEquals(0, mapped.forksCount)
        assertEquals("", mapped.htmlUrl)
        assertEquals("", mapped.userName)
        assertEquals("", mapped.avatarUrl)
        assertEquals(2, mapped.page)
        assertEquals("Java", mapped.language)
    }

    @Test
    fun `remote pull request mapper should map nullable fields to defaults`() {
        val mapped = remotePullRequestMapper.mapToDomain(
            Fixtures.createRemotePullRequest().copy(
                id = null,
                url = null,
                title = null,
                body = null,
                user = null,
            ),
        )

        assertEquals(-1L, mapped.id)
        assertEquals("", mapped.htmlUrl)
        assertEquals("", mapped.title)
        assertEquals("", mapped.body)
        assertEquals("", mapped.userName)
        assertEquals("", mapped.avatarUrl)
    }

    @Test
    fun `local repository mapper should map domain and local both ways`() {
        val local = Fixtures.createLocalGitHubRepository(id = 9, page = 3, language = "Kotlin")
        val domain = localGitHubRepositoryMapper.mapToDomain(local)
        val mappedBack = localGitHubRepositoryMapper.mapToLocal(domain)

        assertEquals(local.id, domain.id)
        assertEquals(local.title, domain.title)
        assertEquals(local.repositoryUrl, domain.htmlUrl)
        assertEquals(local.language, domain.language)

        assertEquals(domain.id, mappedBack.id)
        assertEquals(domain.title, mappedBack.title)
        assertEquals(domain.htmlUrl, mappedBack.repositoryUrl)
        assertEquals(domain.language, mappedBack.language)
    }

    @Test
    fun `local pull request mapper should keep repository paging metadata`() {
        val domain = Fixtures.domainGitHubPullRequest
        val mapped = localGitHubPullRequestMapper.mapToLocal(
            domainPullRequest = domain,
            repositoryId = 44,
            page = 5,
            orderInPage = 2,
        )

        assertEquals(domain.id, mapped.id)
        assertEquals(44, mapped.repositoryId)
        assertEquals(5, mapped.page)
        assertEquals(2, mapped.orderInPage)
        assertEquals(domain.title, mapped.title)
    }
}
