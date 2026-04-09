package com.caioluis.githubpopular.mapper

import com.caioluis.githubpopular.githubrepos.mapper.UiGitHubRepoMapperImpl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UiGitHubRepoMapperImplTest {

    private val mapper = UiGitHubRepoMapperImpl()

    @Test
    fun `map to ui should trim pull suffix and truncate description when needed`() {
        val source = Fixtures.createDomainGitHubRepository().copy(
            description = "d".repeat(330),
            pullsUrl = "https://api.test.com/repos/user/repo/pulls{/number}",
        )

        val mapped = mapper.mapToUi(source)

        assertEquals(source.id, mapped.id)
        assertEquals(source.title, mapped.title)
        assertEquals(source.stargazersCount, mapped.stargazersCount)
        assertEquals(source.forksCount, mapped.forksCount)
        assertEquals(source.userName, mapped.userName)
        assertEquals(source.avatarUrl, mapped.avatarUrl)
        assertEquals(source.htmlUrl, mapped.repositoryUrl)
        assertEquals("https://api.test.com/repos/user/repo/pulls", mapped.pullsUrl)
        assertEquals(203, mapped.description.length)
        assertTrue(mapped.description.endsWith("..."))
    }

    @Test
    fun `map to ui should keep description when it is under limit`() {
        val source = Fixtures.createDomainGitHubRepository().copy(
            description = "Short",
            pullsUrl = "https://api.test.com/repos/user/repo/pulls",
        )

        val mapped = mapper.mapToUi(source)

        assertEquals("Short", mapped.description)
        assertEquals(source.pullsUrl, mapped.pullsUrl)
    }
}
