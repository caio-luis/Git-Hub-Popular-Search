package com.caioluis.githubpopular.data.impl.mapper

import com.caioluis.githubpopular.data.impl.Fixtures
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteGitHubRepositoryMapperImplTest {

    private val remoteGitHubRepositoryMapper = RemoteGitHubRepositoryMapperImpl()

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
}
