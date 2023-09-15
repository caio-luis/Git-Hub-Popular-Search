package com.caioluis.githubpopular.data.remote.implementation

import com.caioluis.githubpopular.data.remote.implementation.Fixtures.remoteGitHubRepositories
import com.caioluis.githubpopular.data.remote.service.GitHubRepositoriesService
import com.caioluis.githubpopular.data.remote.implementation.RemoteSourceImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RemoteSourceImplTest {

    private val gitHubRepositoriesService = mockk<GitHubRepositoriesService>()
    private val remoteSourceImpl = RemoteSourceImpl(gitHubRepositoriesService)

    @Test
    fun `should fetch repositories from remote`() = runBlocking {
        // Given
        val page = 1
        val language = "kotlin"

        coEvery {
            gitHubRepositoriesService.getGitHubRepositories(any(), any(), any())
        } returns remoteGitHubRepositories

        // When
        val result = remoteSourceImpl.fetchFromRemote(page, language)

        // Then
        assertEquals(remoteGitHubRepositories.repositories, result)
    }

    @Test
    fun `should return null when remote response is null`() = runBlocking {
        // Given
        val page = 1
        val language = "java"

        coEvery {
            gitHubRepositoriesService.getGitHubRepositories(any(), any(), any())
        } returns null

        // When
        val result = remoteSourceImpl.fetchFromRemote(page, language)

        // Then
        assertNull(result)
    }
}
