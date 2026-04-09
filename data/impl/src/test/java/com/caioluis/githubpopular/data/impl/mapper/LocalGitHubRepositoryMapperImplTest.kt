package com.caioluis.githubpopular.data.impl.mapper

import com.caioluis.githubpopular.data.impl.Fixtures
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalGitHubRepositoryMapperImplTest {

    private val localGitHubRepositoryMapper = LocalGitHubRepositoryMapperImpl()

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
}
