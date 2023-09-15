package com.caioluis.githubpopular.data.remote.implementation

import com.caioluis.githubpopular.data.local.model.LocalGitHubRepository
import com.caioluis.githubpopular.data.local.model.LocalRepositoryOwner
import com.caioluis.githubpopular.data.remote.model.RemoteGitHubRepositories
import com.caioluis.githubpopular.data.remote.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.remote.model.RemoteRepositoryOwner
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.entity.DomainRepositoryOwner

object Fixtures {
    const val page = 1
    const val language = "kotlin"

    val remoteGitHubRepositories = RemoteGitHubRepositories(
        listOf(
            RemoteGitHubRepository(
                id = 1,
                name = "test-name",
                fullName = "test-full-name",
                owner = RemoteRepositoryOwner(
                    login = "test-login",
                    id = 2,
                    avatarUrl = "test-avatar-url"
                ),
                description = "test-description",
                pullsUrl = "test-pulls-url",
                stargazersCount = 100,
                forksCount = 50,
                htmlUrl = "test",
            )
        )
    )

    val localGitHubRepositories = listOf(
        LocalGitHubRepository(
            id = 1,
            name = "test-name",
            fullName = "test-full-name",
            owner = LocalRepositoryOwner(
                id = 2,
                login = "test-login",
                avatarUrl = "test-avatar-url"
            ),
            description = "test-description",
            pullsUrl = "test-pulls-url",
            stargazersCount = 100,
            forksCount = 50,
            htmlUrl = "test",
            page = 1,
            language = "kotlin"
        )
    )

    val domainGitHubRepositories = listOf(
        DomainGitHubRepository(
            id = 1,
            name = "test-name",
            fullName = "test-full-name",
            owner = DomainRepositoryOwner(
                id = 2,
                login = "test-login",
                avatarUrl = "test-avatar-url"
            ),
            description = "test-description",
            pullsUrl = "test-pulls-url",
            stargazersCount = 100,
            forksCount = 50,
            htmlUrl = "test",
            page = 1,
            language = "kotlin"
        )
    )
}