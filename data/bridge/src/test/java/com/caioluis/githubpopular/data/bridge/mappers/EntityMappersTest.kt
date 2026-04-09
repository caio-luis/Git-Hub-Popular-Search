package com.caioluis.githubpopular.data.bridge.mappers

import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EntityMappersTest {

    @Test
    fun `LocalGitHubRepositoryMapper contract should expose both directions`() {
        val mapper = object : LocalGitHubRepositoryMapper {
            override fun mapToDomain(localRepository: LocalGitHubRepository) = MappersFixtures.domainGitHubRepository

            override fun mapToLocal(domainRepository: DomainGitHubRepository) = MappersFixtures.localGitHubRepository
        }

        assertEquals(
            MappersFixtures.domainGitHubRepository,
            mapper.mapToDomain(MappersFixtures.localGitHubRepository),
        )
        assertEquals(
            MappersFixtures.localGitHubRepository,
            mapper.mapToLocal(MappersFixtures.domainGitHubRepository),
        )
    }

    @Test
    fun `LocalGitHubPullRequestMapper contract should preserve pagination arguments`() {
        val mapper = object : LocalGitHubPullRequestMapper {
            override fun mapToDomain(localPullRequest: LocalGitHubPullRequest) = MappersFixtures.domainGitHubPullRequest

            override fun mapToLocal(
                domainPullRequest: DomainGitHubPullRequest,
                repositoryId: Int,
                page: Int,
                orderInPage: Int,
            ) = MappersFixtures.localGitHubPullRequest.copy(
                repositoryId = repositoryId,
                page = page,
                orderInPage = orderInPage,
            )
        }

        val mapped = mapper.mapToLocal(
            domainPullRequest = MappersFixtures.domainGitHubPullRequest,
            repositoryId = 5,
            page = 2,
            orderInPage = 3,
        )

        assertEquals(5, mapped.repositoryId)
        assertEquals(2, mapped.page)
        assertEquals(3, mapped.orderInPage)
    }
}
