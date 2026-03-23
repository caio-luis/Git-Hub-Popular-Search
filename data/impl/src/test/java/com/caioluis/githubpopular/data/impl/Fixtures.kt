package com.caioluis.githubpopular.data.impl

import com.caioluis.githubpopular.data.bridge.local.model.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.bridge.local.model.LocalGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.model.RemotePullRequest
import com.caioluis.githubpopular.data.bridge.remote.model.RemoteRepositoryOwner
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest

object Fixtures {

    const val REPOSITORY_ID = 1

    val domainGitHubPullRequest = DomainGitHubPullRequest(
        id = 1L,
        htmlUrl = "url",
        title = "title",
        body = "body",
        userName = "user",
        avatarUrl = "avatar",
    )

    val localGitHubPullRequest = LocalGitHubPullRequest(
        id = 1L,
        htmlUrl = "url",
        title = "title",
        body = "body",
        userName = "user",
        avatarUrl = "avatar",
        repositoryId = REPOSITORY_ID,
    )

    fun createLocalGitHubPullRequest(
        id: Long = 1L,
        repositoryId: Int = REPOSITORY_ID,
    ) = LocalGitHubPullRequest(
        id = id,
        htmlUrl = "http://example.com/$id",
        title = "PR $id",
        body = "Body $id",
        userName = "user$id",
        avatarUrl = "avatar$id",
        repositoryId = repositoryId,
    )

    val localGitHubRepository = LocalGitHubRepository(
        id = 1,
        title = "Test Repo",
        description = "Description",
        pullsUrl = "pulls_url",
        stargazersCount = 100,
        forksCount = 10,
        repositoryUrl = "url",
        page = 1,
        language = "Kotlin",
        userName = "user",
        avatarUrl = "avatar",
    )

    fun createLocalGitHubRepository(
        id: Int = 1,
        page: Int = 1,
        language: String = "Kotlin",
        stargazersCount: Int = 100,
    ) = LocalGitHubRepository(
        id = id,
        title = "Test Repo $id",
        description = "Description $id",
        pullsUrl = "pulls_url_$id",
        stargazersCount = stargazersCount,
        forksCount = 10,
        repositoryUrl = "url_$id",
        page = page,
        language = language,
        userName = "user$id",
        avatarUrl = "avatar$id",
    )

    fun createRemotePullRequest(
        id: Long = 1L,
    ) = RemotePullRequest(
        id = id,
        title = "title",
        url = "url",
        body = "body",
        user = RemoteRepositoryOwner(
            login = "user",
            avatarUrl = "avatar",
        ),
    )
}
