package com.caioluis.githubpopular.data.local

import com.caioluis.githubpopular.data.MainDispatcherRule
import com.caioluis.githubpopular.data.remote.implementation.Fixtures.domainGitHubRepositories
import com.caioluis.githubpopular.data.remote.implementation.Fixtures.language
import com.caioluis.githubpopular.data.remote.implementation.Fixtures.localGitHubRepositories
import com.caioluis.githubpopular.data.local.GitHubRepositoriesDao
import com.caioluis.githubpopular.data.local.LocalSourceImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LocalSourceImplTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private val daoMock = mockk<GitHubRepositoriesDao>()
    private val localSourceImpl = LocalSourceImpl(daoMock)

    @Test
    fun `when page is less than or equal to 1, should delete all repositories and save new ones`() =
        runTest {
            coEvery { daoMock.deleteReposByLanguage(any()) } returns Unit
            coEvery { daoMock.saveRepositories(any()) } returns Unit
            coEvery { daoMock.getAllRepositories(any(), any()) } returns null

            runBlocking {
                localSourceImpl.saveToLocalCache(
                    repositories = domainGitHubRepositories,
                    page = 1,
                    language = language
                )
            }

            coVerify(exactly = 1) { daoMock.deleteReposByLanguage(language) }
            coVerify(exactly = 1) { daoMock.saveRepositories(localGitHubRepositories) }

            localSourceImpl.saveToLocalCache(domainGitHubRepositories, 1, language)
            val result = localSourceImpl.getFromCache(1, language)
            assertNull(result)
        }

    @Test
    fun `when page is greater than 1, should not delete repositories and return saved ones`() =
        runTest {
            coEvery { daoMock.deleteReposByLanguage(any()) } returns Unit
            coEvery { daoMock.saveRepositories(any()) } returns Unit
            coEvery { daoMock.getAllRepositories(any(), any()) } returns localGitHubRepositories

            localSourceImpl.saveToLocalCache(domainGitHubRepositories, 2, language)

            coVerify(exactly = 0) { daoMock.deleteReposByLanguage(language) }
            coVerify(exactly = 1) { daoMock.saveRepositories(localGitHubRepositories) }

            localSourceImpl.saveToLocalCache(domainGitHubRepositories, 2, language)
            val result = localSourceImpl.getFromCache(2, language)

            assertNotNull(result)
            assertEquals(domainGitHubRepositories, result)
        }

    @Test
    fun `get from cache should not delete repositories and return saved ones`() =
        runTest {
            coEvery { daoMock.getAllRepositories(any(), any()) } returns localGitHubRepositories

            localSourceImpl.getFromCache(1, language)

            coVerify(exactly = 0) { daoMock.deleteReposByLanguage(language) }
            coVerify(exactly = 0) { daoMock.saveRepositories(localGitHubRepositories) }
            coVerify(exactly = 1) { daoMock.getAllRepositories(1, language) }

            val result = localSourceImpl.getFromCache(1, language)

            assertNotNull(result)
            assertEquals(domainGitHubRepositories, result)
        }
}
