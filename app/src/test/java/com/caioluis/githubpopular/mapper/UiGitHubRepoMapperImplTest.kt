package com.caioluis.githubpopular.mapper

import com.caioluis.githubpopular.githubrepos.mapper.UiGitHubRepoMapperImpl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UiGitHubRepoMapperImplTest {

    private val mapper = UiGitHubRepoMapperImpl()

    @Test
    fun mapToUiShouldTrimPullSuffixAndTruncateDescriptionWhenNeeded() {
        val source = Fixtures.createDomainGitHubRepository().copy(
            description = "d".repeat(330),
            pullsUrl = "https://api.github.com/repos/user/repo/pulls{/number}",
        )

        val mapped = mapper.mapToUi(source)

        assertEquals(source.id, mapped.id)
        assertEquals(source.title, mapped.title)
        assertEquals(source.stargazersCount, mapped.stargazersCount)
        assertEquals(source.forksCount, mapped.forksCount)
        assertEquals(source.userName, mapped.userName)
        assertEquals(source.avatarUrl, mapped.avatarUrl)
        assertEquals(source.htmlUrl, mapped.repositoryUrl)
        assertEquals("https://api.github.com/repos/user/repo/pulls", mapped.pullsUrl)
        assertEquals(303, mapped.description.length)
        assertTrue(mapped.description.endsWith("..."))
    }

    @Test
    fun mapToUiShouldKeepDescriptionWhenItIsUnderLimit() {
        val source = Fixtures.createDomainGitHubRepository().copy(
            description = "Short",
            pullsUrl = "https://api.github.com/repos/user/repo/pulls",
        )

        val mapped = mapper.mapToUi(source)

        assertEquals("Short", mapped.description)
        assertEquals(source.pullsUrl, mapped.pullsUrl)
    }
}
