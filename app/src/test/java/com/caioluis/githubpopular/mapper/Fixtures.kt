package com.caioluis.githubpopular.mapper

import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.entity.DomainRepositoryOwner
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.model.UiRepositoryOwner

object Fixtures {
    val domainRepositoryOwner = DomainRepositoryOwner(
        login = "user",
        id = 123,
        avatarUrl = "https://example.com/avatar.jpg"
    )

    val domainGitHubRepository = DomainGitHubRepository(
        id = 123,
        name = "example-repo",
        fullName = "user/example-repo",
        owner = domainRepositoryOwner,
        description = "This is an example repository",
        pullsUrl = "https://api.github.com/repos/user/example-repo/pulls",
        stargazersCount = 42,
        forksCount = 10,
        page = 1
    )

    val uiRepositoryOwner = UiRepositoryOwner(
        login = "user",
        id = 123,
        avatarUrl = "https://example.com/avatar.jpg"
    )

    val uiRepository = UiGitHubRepository(
        id = 123,
        name = "example-repo",
        fullName = "user/example-repo",
        owner = uiRepositoryOwner,
        description = "This is an example repository",
        pullsUrl = "https://api.github.com/repos/user/example-repo/pulls",
        stargazersCount = 42,
        forksCount = 10,
    )
}
