package com.caioluis.githubpopular.mapper

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.githubrepos.model.UiGitHubRepo

object Fixtures {

    const val REPOSITORY_ID = 123

    fun createDomainGitHubRepository(
        id: Int = REPOSITORY_ID,
    ) = DomainGitHubRepository(
        id = id,
        title = "example-repo-$id",
        description = "This is an example repository",
        pullsUrl = "https://api.github.com/repos/user/example-repo/pulls",
        stargazersCount = 42,
        forksCount = 10,
        page = 1,
        userName = "user",
        avatarUrl = "https://example.com/avatar.jpg",
        htmlUrl = "https://github.com/user/example-repo",
    )

    fun createUiGitHubRepo(
        id: Int = REPOSITORY_ID,
    ) = UiGitHubRepo(
        id = id,
        title = "example-repo-$id",
        description = "This is an example repository",
        pullsUrl = "https://api.github.com/repos/user/example-repo/pulls",
        stargazersCount = 42,
        forksCount = 10,
        userName = "user",
        avatarUrl = "https://example.com/avatar.jpg",
        repositoryUrl = "https://github.com/user/example-repo",
    )
}
