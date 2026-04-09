package com.caioluis.githubpopular.data.impl.mapper

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
}
