package com.caioluis.data.local

import com.caioluis.data.MainDispatcherRule
import com.caioluis.data.remote.implementation.Fixtures.domainGitHubRepositories
import com.caioluis.data.remote.implementation.Fixtures.localGitHubRepositories
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
        runBlocking {
            coEvery { daoMock.deleteAllGitHubRepositories() } returns Unit
            coEvery { daoMock.saveRepositories(any()) } returns Unit
            coEvery { daoMock.getAllRepositories(any()) } returns null

            runBlocking { localSourceImpl.getFromLocalDatabase(domainGitHubRepositories, 1) }

            coVerify(exactly = 1) { daoMock.deleteAllGitHubRepositories() }
            coVerify(exactly = 1) { daoMock.saveRepositories(localGitHubRepositories) }

            val result = localSourceImpl.getFromLocalDatabase(domainGitHubRepositories, 1)
            assertNull(result)
        }

    @Test
    fun `when page is greater than 1, should not delete repositories and return saved ones`() =
        runBlocking {
            coEvery { daoMock.deleteAllGitHubRepositories() } returns Unit
            coEvery { daoMock.saveRepositories(any()) } returns Unit
            coEvery { daoMock.getAllRepositories(any()) } returns localGitHubRepositories

            localSourceImpl.getFromLocalDatabase(domainGitHubRepositories, 2)

            coVerify(exactly = 0) { daoMock.deleteAllGitHubRepositories() }
            coVerify(exactly = 1) { daoMock.saveRepositories(localGitHubRepositories) }

            val result = localSourceImpl.getFromLocalDatabase(domainGitHubRepositories, 2)

            assertNotNull(result)
            assertEquals(domainGitHubRepositories, result)
        }
}
