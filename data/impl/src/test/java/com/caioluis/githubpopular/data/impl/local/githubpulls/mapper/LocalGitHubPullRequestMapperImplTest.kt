package com.caioluis.githubpopular.data.impl.local.githubpulls.mapper

import com.caioluis.githubpopular.data.impl.Fixtures
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalGitHubPullRequestMapperImplTest {

    private val localGitHubPullRequestMapper = LocalGitHubPullRequestMapperImpl()

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

    @Test
    fun `local pull request mapper should map local to domain`() {
        val local = Fixtures.createLocalGitHubPullRequest(id = 99L, repositoryId = 9)

        val mapped = localGitHubPullRequestMapper.mapToDomain(local)

        assertEquals(local.id, mapped.id)
        assertEquals(local.htmlUrl, mapped.htmlUrl)
        assertEquals(local.title, mapped.title)
        assertEquals(local.body, mapped.body)
        assertEquals(local.userName, mapped.userName)
        assertEquals(local.avatarUrl, mapped.avatarUrl)
    }
}
