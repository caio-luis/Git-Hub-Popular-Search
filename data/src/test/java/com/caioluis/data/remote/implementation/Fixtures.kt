package com.caioluis.data.remote.implementation

import com.caioluis.data.local.model.LocalGitHubRepository
import com.caioluis.data.local.model.LocalRepositoryOwner

object Fixtures {
    val repositories = listOf(
        LocalGitHubRepository(
            id = 1,
            name = "Repo1",
            fullName = "user1/Repo1",
            owner = LocalRepositoryOwner(
                id = 1,
                login = "user1",
                avatarUrl = "avatarUrl1"
            ),
            description = "Description1",
            pullsUrl = "http://api.github.com/repos/user1/repo1/pulls",
            stargazersCount = 5,
            forksCount = 3,
            page = 1
        ),
        LocalGitHubRepository(
            id = 2,
            name = "Repo2",
            fullName = "user1/Repo2",
            owner = LocalRepositoryOwner(
                id = 1,
                login = "user1",
                avatarUrl = "avatarUrl1"
            ),
            description = "Description2",
            pullsUrl = "http://api.github.com/repos/user1/repo2/pulls",
            stargazersCount = 10,
            forksCount = 2,
            page = 1
        ),
        LocalGitHubRepository(
            id = 3,
            name = "Repo3",
            fullName = "user1/Repo3",
            owner = LocalRepositoryOwner(
                id = 1,
                login = "user1",
                avatarUrl = "avatarUrl1"
            ),
            description = "Description3",
            pullsUrl = "http://api.github.com/repos/user1/repo2/pulls",
            stargazersCount = 10,
            forksCount = 2,
            page = 2
        ),
    )
}
