package com.caioluis.githubpopular.data.impl

import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.PullRequestRemoteKey
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.GitHubReposRemoteKey
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.githubpulls.model.RemotePullRequest
import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteGitHubRepositories
import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteRepositoryOwner
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest

object Fixtures {

    const val REPOSITORY_ID = 1
    const val DEFAULT_LANGUAGE = "Kotlin"
    const val STARTING_PAGE = 1

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
        page = STARTING_PAGE,
        orderInPage = 0,
    )

    fun createLocalGitHubPullRequest(
        id: Long = 1L,
        repositoryId: Int = REPOSITORY_ID,
        page: Int = STARTING_PAGE,
        orderInPage: Int = 0,
    ) = LocalGitHubPullRequest(
        id = id,
        htmlUrl = "http://example.com/$id",
        title = "PR $id",
        body = "Body $id",
        userName = "user$id",
        avatarUrl = "avatar$id",
        repositoryId = repositoryId,
        page = page,
        orderInPage = orderInPage,
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

    fun createRemoteGitHubRepository(
        id: Int = REPOSITORY_ID,
    ) = RemoteGitHubRepository(
        id = id,
        name = "Repo $id",
        fullName = "User/Repo$id",
        owner = RemoteRepositoryOwner(login = "user", avatarUrl = "avatar"),
        description = "Description",
        pullsUrl = "https://api.github.com/repos/user/repo$id/pulls",
        stargazersCount = 100,
        forksCount = 10,
        htmlUrl = "https://github.com/user/repo$id",
    )

    fun createLocalGitHubRepository(
        id: Int = REPOSITORY_ID,
        language: String = DEFAULT_LANGUAGE,
        page: Int = STARTING_PAGE,
    ) = LocalGitHubRepository(
        id = id,
        title = "Repo $id",
        description = "Description",
        pullsUrl = "https://api.github.com/repos/user/repo$id/pulls",
        stargazersCount = 100,
        forksCount = 10,
        repositoryUrl = "https://github.com/user/repo$id",
        page = page,
        language = language,
        userName = "user",
        avatarUrl = "avatar",
    )

    fun createRemoteGitHubRepositoriesResponse(
        repositories: List<RemoteGitHubRepository> = listOf(createRemoteGitHubRepository()),
    ) = RemoteGitHubRepositories(
        repositories = repositories,
    )

    fun createRemoteKey(
        language: String = DEFAULT_LANGUAGE,
        nextPage: Int? = 2,
    ) = GitHubReposRemoteKey(
        queryLanguage = language,
        nextPage = nextPage,
    )

    fun createPullRequestRemoteKey(
        repositoryId: Int = REPOSITORY_ID,
        nextPage: Int? = 2,
    ) = PullRequestRemoteKey(
        repositoryId = repositoryId,
        nextPage = nextPage,
    )
}
