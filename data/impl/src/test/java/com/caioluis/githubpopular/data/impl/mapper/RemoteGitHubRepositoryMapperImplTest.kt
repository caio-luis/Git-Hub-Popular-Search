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

    @Test
    fun `remote repository mapper should map all available fields`() {
        val remote = Fixtures.createRemoteGitHubRepository(id = 77)

        val mapped = remoteGitHubRepositoryMapper.mapToDomain(
            remoteRepository = remote,
            page = 4,
            language = "Kotlin",
        )

        assertEquals(77, mapped.id)
        assertEquals(remote.name, mapped.title)
        assertEquals(remote.description, mapped.description)
        assertEquals(remote.pullsUrl, mapped.pullsUrl)
        assertEquals(remote.stargazersCount, mapped.stargazersCount)
        assertEquals(remote.forksCount, mapped.forksCount)
        assertEquals(remote.htmlUrl, mapped.htmlUrl)
        assertEquals(remote.owner?.login, mapped.userName)
        assertEquals(remote.owner?.avatarUrl, mapped.avatarUrl)
        assertEquals(4, mapped.page)
        assertEquals("Kotlin", mapped.language)
    }
}
