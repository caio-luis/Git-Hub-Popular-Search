package com.caioluis.githubpopular.mapper

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.model.UiGitHubRepository

object Fixtures {
    val domainGitHubRepository =
        DomainGitHubRepository(
            id = 123,
            title = "example-repo",
            description = "This is an example repository",
            pullsUrl = "https://api.github.com/repos/user/example-repo/pulls",
            stargazersCount = 42,
            forksCount = 10,
            page = 1,
            userName = "user",
            avatarUrl = "https://example.com/avatar.jpg",
            htmlUrl = "https://github.com/user/example-repo",
        )

    val uiRepository =
        UiGitHubRepository(
            id = 123,
            title = "example-repo",
            description = "This is an example repository",
            pullsUrl = "https://api.github.com/repos/user/example-repo/pulls",
            stargazersCount = 42,
            forksCount = 10,
            userName = "user",
            avatarUrl = "https://example.com/avatar.jpg",
            repositoryUrl = "https://github.com/user/example-repo",
        )
}
