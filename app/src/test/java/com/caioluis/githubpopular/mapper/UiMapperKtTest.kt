package com.caioluis.githubpopular.mapper

import com.caioluis.githubpopular.mapper.Fixtures.domainGitHubRepository
import com.caioluis.githubpopular.mapper.Fixtures.domainRepositoryOwner
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
        assertEquals(domainGitHubRepository.name, uiGitHubRepository.name)
        assertEquals(domainGitHubRepository.fullName, uiGitHubRepository.fullName)
        assertEquals(domainGitHubRepository.pullsUrl, uiGitHubRepository.pullsUrl)
        assertEquals(domainGitHubRepository.owner.id, uiGitHubRepository.owner.id)
        assertEquals(domainGitHubRepository.forksCount, uiGitHubRepository.forksCount)
        assertEquals(domainGitHubRepository.description, uiGitHubRepository.description)
        assertEquals(domainGitHubRepository.owner.login, uiGitHubRepository.owner.login)
        assertEquals(domainGitHubRepository.owner.avatarUrl, uiGitHubRepository.owner.avatarUrl)
        assertEquals(domainGitHubRepository.stargazersCount, uiGitHubRepository.stargazersCount)
    }

    @Test
    fun `assert that map domain repository owner to ui repository owner`() {
        // When
        val uiRepositoryOwner = domainRepositoryOwner.toUi()

        // Then
        assertEquals(domainRepositoryOwner.id, uiRepositoryOwner.id)
        assertEquals(domainRepositoryOwner.login, uiRepositoryOwner.login)
        assertEquals(domainRepositoryOwner.avatarUrl, uiRepositoryOwner.avatarUrl)
    }

    @Test
    fun `assert that map domain repository to ui repository`() {
        // When
        val uiGitHubRepository = domainGitHubRepository.toUi()

        // Then
        assertEquals(uiRepository, uiGitHubRepository)
    }
}
