package com.caioluis.githubpopular.mapper

import com.caioluis.githubpopular.githubpulls.mapper.PullRequestUiMapperImpl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PullRequestUiMapperImplTest {

    private val mapper = PullRequestUiMapperImpl()

    @Test
    fun `map to ui should truncate body and keep essential fields`() {
        val source = Fixtures.createDomainGitHubPullRequest().copy(
            body = "a".repeat(260),
            title = "Improve cache fallback",
        )

        val mapped = mapper.mapToUi(source)

        assertEquals(source.id, mapped.id)
        assertEquals(source.title, mapped.title)
        assertEquals(source.htmlUrl, mapped.htmlUrl)
        assertEquals(source.userName, mapped.userName)
        assertEquals(source.avatarUrl, mapped.avatarUrl)
        assertEquals(203, mapped.body.length)
        assertTrue(mapped.body.endsWith("..."))
    }

    @Test
    fun `map to ui should keep body when under limit`() {
        val source = Fixtures.createDomainGitHubPullRequest().copy(body = "short body")

        val mapped = mapper.mapToUi(source)

        assertEquals("short body", mapped.body)
    }
}
