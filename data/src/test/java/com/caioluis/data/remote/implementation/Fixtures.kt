package com.caioluis.data.remote.implementation

import com.caioluis.data.local.model.LocalGitHubRepository
import com.caioluis.data.local.model.LocalRepositoryOwner
import com.caioluis.data.remote.model.RemoteGitHubRepositories
import com.caioluis.data.remote.model.RemoteGitHubRepository
import com.caioluis.data.remote.model.RemoteRepositoryOwner

object Fixtures {
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
                forksCount = 50
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
            page = 1
        )
    )
}
