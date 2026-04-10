package com.caioluis.githubpopular.data.impl.local.githubrepos.mapper

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
        assertEquals(local.description, domain.description)
        assertEquals(local.pullsUrl, domain.pullsUrl)
        assertEquals(local.stargazersCount, domain.stargazersCount)
        assertEquals(local.forksCount, domain.forksCount)
        assertEquals(local.repositoryUrl, domain.htmlUrl)
        assertEquals(local.page, domain.page)
        assertEquals(local.language, domain.language)
        assertEquals(local.userName, domain.userName)
        assertEquals(local.avatarUrl, domain.avatarUrl)

        assertEquals(domain.id, mappedBack.id)
        assertEquals(domain.title, mappedBack.title)
        assertEquals(domain.description, mappedBack.description)
        assertEquals(domain.pullsUrl, mappedBack.pullsUrl)
        assertEquals(domain.stargazersCount, mappedBack.stargazersCount)
        assertEquals(domain.forksCount, mappedBack.forksCount)
        assertEquals(domain.htmlUrl, mappedBack.repositoryUrl)
        assertEquals(domain.page, mappedBack.page)
        assertEquals(domain.language, mappedBack.language)
        assertEquals(domain.userName, mappedBack.userName)
        assertEquals(domain.avatarUrl, mappedBack.avatarUrl)
    }
}
