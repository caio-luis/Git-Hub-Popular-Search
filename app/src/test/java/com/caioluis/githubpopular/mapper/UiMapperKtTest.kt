package com.caioluis.githubpopular.mapper

import com.caioluis.githubpopular.mapper.Fixtures.domainGitHubRepository
import com.caioluis.githubpopular.mapper.Fixtures.uiRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class UiMapperKtTest {
    @Test
    fun `assert that map domain to ui model`() {
        // When
        val uiGitHubRepository = domainGitHubRepository.toUi()

        // Then
        assertEquals(domainGitHubRepository.id, uiGitHubRepository.id)
        assertEquals(domainGitHubRepository.title, uiGitHubRepository.title)
        assertEquals(domainGitHubRepository.description, uiGitHubRepository.description)
        assertEquals(domainGitHubRepository.pullsUrl, uiGitHubRepository.pullsUrl)
        assertEquals(domainGitHubRepository.stargazersCount, uiGitHubRepository.stargazersCount)
        assertEquals(domainGitHubRepository.forksCount, uiGitHubRepository.forksCount)
        assertEquals(domainGitHubRepository.htmlUrl, uiGitHubRepository.repositoryUrl)
        assertEquals(domainGitHubRepository.userName, uiGitHubRepository.userName)
        assertEquals(domainGitHubRepository.avatarUrl, uiGitHubRepository.avatarUrl)
    }

    @Test
    fun `assert that map domain repository to ui repository`() {
        // When
        val uiGitHubRepository = domainGitHubRepository.toUi()

        // Then
        assertEquals(uiRepository, uiGitHubRepository)
    }
}
