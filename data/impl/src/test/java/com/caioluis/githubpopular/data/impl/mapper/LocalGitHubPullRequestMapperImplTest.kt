package com.caioluis.githubpopular.data.impl.mapper

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
}
