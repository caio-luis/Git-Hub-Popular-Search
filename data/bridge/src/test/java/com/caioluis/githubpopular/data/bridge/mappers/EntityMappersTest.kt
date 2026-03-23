package com.caioluis.githubpopular.data.bridge.mappers

import com.caioluis.githubpopular.data.bridge.mappers.MappersFixtures.assertDefaultRemotePullRequestMapping
import com.caioluis.githubpopular.data.bridge.mappers.MappersFixtures.assertDefaultRemoteRepositoryMapping
import com.caioluis.githubpopular.data.bridge.mappers.MappersFixtures.assertRemotePullRequestMapping
import com.caioluis.githubpopular.data.bridge.mappers.MappersFixtures.assertRemoteRepositoryMapping
import com.caioluis.githubpopular.data.bridge.remote.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.model.RemoteRepositoryOwner
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class EntityMappersTest {

    @TestFactory
    fun `RemoteGitHubRepository toDomain scenarios`(): List<DynamicTest> {
        val page = 1
        val language = "Kotlin"

        val scenarios = listOf(
            "Mapping with all fields present" to MappersFixtures.remoteGitHubRepository,
            "Mapping with all null fields" to MappersFixtures.remoteGitHubRepositoryNull,
            "Mapping with owner present but owner fields null" to MappersFixtures.remoteGitHubRepositoryOwnerNullFields,
        )

        return scenarios.map { (name, remote) ->
            dynamicTest(name) {
                val domain = remote.toDomain(page, language)
                if (remote.id != null) {
                    assertRemoteRepositoryMapping(remote, domain, page, language)
                } else {
                    assertDefaultRemoteRepositoryMapping(domain, page, language)
                }
            }
        }
    }

    @TestFactory
    fun `RemotePullRequest toDomain scenarios`(): List<DynamicTest> {
        val scenarios = listOf(
            "Mapping with all fields present" to MappersFixtures.remotePullRequest,
            "Mapping with all null fields" to MappersFixtures.remotePullRequestNull,
            "Mapping with user present but user fields null" to MappersFixtures.remotePullRequestUserNullFields,
        )

        return scenarios.map { (name, remote) ->
            dynamicTest(name) {
                val domain = remote.toDomain()
                if (remote.id != null) {
                    assertRemotePullRequestMapping(remote, domain)
                } else {
                    assertDefaultRemotePullRequestMapping(domain)
                }
            }
        }
    }

    @TestFactory
    fun `Local models toDomain scenarios`(): List<DynamicTest> {
        val scenarios = listOf(
            "LocalGitHubRepository toDomain" to {
                val local = MappersFixtures.localGitHubRepository
                val domain = local.toDomain()
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
            },
            "LocalGitHubPullRequest toDomain" to {
                val local = MappersFixtures.localGitHubPullRequest
                val domain = local.toDomain()
                assertEquals(local.id, domain.id)
                assertEquals(local.htmlUrl, domain.htmlUrl)
                assertEquals(local.title, domain.title)
                assertEquals(local.body, domain.body)
                assertEquals(local.userName, domain.userName)
                assertEquals(local.avatarUrl, domain.avatarUrl)
            },
        )

        return scenarios.map { (name, testBlock) ->
            dynamicTest(name) { testBlock() }
        }
    }

    @TestFactory
    fun `Domain models toLocal scenarios`(): List<DynamicTest> {
        val scenarios = listOf(
            "DomainGitHubRepository toLocal" to {
                val domain = MappersFixtures.domainGitHubRepository
                val local = domain.toLocal()
                assertEquals(domain.id, local.id)
                assertEquals(domain.title, local.title)
                assertEquals(domain.description, local.description)
                assertEquals(domain.pullsUrl, local.pullsUrl)
                assertEquals(domain.stargazersCount, local.stargazersCount)
                assertEquals(domain.forksCount, local.forksCount)
                assertEquals(domain.htmlUrl, local.repositoryUrl)
                assertEquals(domain.page, local.page)
                assertEquals(domain.language, local.language)
                assertEquals(domain.userName, local.userName)
                assertEquals(domain.avatarUrl, local.avatarUrl)
            },
            "DomainGitHubPullRequest toLocal" to {
                val domain = MappersFixtures.domainGitHubPullRequest
                val repoId = 999
                val local = domain.toLocal(repoId)
                assertEquals(domain.id, local.id)
                assertEquals(domain.htmlUrl, local.htmlUrl)
                assertEquals(domain.title, local.title)
                assertEquals(domain.body, local.body)
                assertEquals(domain.userName, local.userName)
                assertEquals(domain.avatarUrl, local.avatarUrl)
                assertEquals(repoId, local.repositoryId)
            },
        )

        return scenarios.map { (name, testBlock) ->
            dynamicTest(name) { testBlock() }
        }
    }

    @TestFactory
    fun `MockK specialized scenarios`(): List<DynamicTest> {
        val scenarios = listOf(
            "Mapping RemoteRepositoryOwner using MockK" to {
                val owner = mockk<RemoteRepositoryOwner> {
                    every { login } returns "mock_user"
                    every { avatarUrl } returns "mock_avatar"
                }
                val remote = RemoteGitHubRepository(id = 123, owner = owner)
                val domain = remote.toDomain(1, "Kotlin")
                assertEquals("mock_user", domain.userName)
                assertEquals("mock_avatar", domain.avatarUrl)
                assertEquals(123, domain.id)
            },
        )

        return scenarios.map { (name, testBlock) ->
            dynamicTest(name) { testBlock() }
        }
    }
}
