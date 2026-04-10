package com.caioluis.githubpopular.data.impl.remote.githubpulls.mapper

import com.caioluis.githubpopular.data.impl.Fixtures
import org.junit.Assert.assertEquals
import org.junit.Test

class RemotePullRequestMapperImplTest {

    private val remotePullRequestMapper = RemotePullRequestMapperImpl()

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
    fun `remote pull request mapper should map all available fields`() {
        val remote = Fixtures.createRemotePullRequest(id = 88L)

        val mapped = remotePullRequestMapper.mapToDomain(remote)

        assertEquals(88L, mapped.id)
        assertEquals(remote.url, mapped.htmlUrl)
        assertEquals(remote.title, mapped.title)
        assertEquals(remote.body, mapped.body)
        assertEquals(remote.user?.login, mapped.userName)
        assertEquals(remote.user?.avatarUrl, mapped.avatarUrl)
    }
}
