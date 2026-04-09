package com.caioluis.githubpopular.data.bridge.mappers

import com.caioluis.githubpopular.data.bridge.local.githubpulls.entity.LocalGitHubPullRequest
import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.githubpulls.model.RemotePullRequest
import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteGitHubRepository
import com.caioluis.githubpopular.data.bridge.remote.githubrepos.model.RemoteRepositoryOwner
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import org.junit.jupiter.api.Assertions.assertEquals

object MappersFixtures {

    val remoteGitHubRepository = RemoteGitHubRepository(
        id = 1,
        name = "Repo",
        description = "Desc",
        pullsUrl = "url",
        stargazersCount = 10,
        forksCount = 5,
        htmlUrl = "html",
        owner = RemoteRepositoryOwner(login = "user", avatarUrl = "avatar"),
    )

    val remoteGitHubRepositoryNull = RemoteGitHubRepository(
        id = null,
        name = null,
        description = null,
        pullsUrl = null,
        stargazersCount = null,
        forksCount = null,
        htmlUrl = null,
        owner = null,
    )

    val remoteGitHubRepositoryOwnerNullFields = RemoteGitHubRepository(
        owner = RemoteRepositoryOwner(login = null, avatarUrl = null),
    )

    val remotePullRequest = RemotePullRequest(
        id = 100L,
        url = "pr_url",
        title = "PR Title",
        body = "PR Body",
        user = RemoteRepositoryOwner(login = "dev", avatarUrl = "dev_avatar"),
    )

    val remotePullRequestNull = RemotePullRequest(
        id = null,
        url = null,
        title = null,
        body = null,
        user = null,
    )

    val remotePullRequestUserNullFields = RemotePullRequest(
        user = RemoteRepositoryOwner(login = null, avatarUrl = null),
    )

    val localGitHubRepository = LocalGitHubRepository(
        id = 1,
        title = "Title",
        description = "Desc",
        pullsUrl = "pulls",
        stargazersCount = 100,
        forksCount = 50,
        repositoryUrl = "repo_url",
        page = 2,
        language = "Java",
        userName = "owner",
        avatarUrl = "avatar",
    )

    val localGitHubPullRequest = LocalGitHubPullRequest(
        id = 1L,
        htmlUrl = "url",
        title = "PR",
        body = "Body",
        userName = "user",
        avatarUrl = "avatar",
        repositoryId = 123,
        page = 1,
        orderInPage = 0,
    )

    val domainGitHubRepository = DomainGitHubRepository(
        id = 1,
        title = "Title",
        description = "Desc",
        pullsUrl = "pulls",
        stargazersCount = 100,
        forksCount = 50,
        htmlUrl = "html",
        page = 2,
        language = "Kotlin",
        userName = "user",
        avatarUrl = "avatar",
    )

    val domainGitHubPullRequest = DomainGitHubPullRequest(
        id = 1L,
        htmlUrl = "url",
        title = "PR",
        body = "Body",
        userName = "user",
        avatarUrl = "avatar",
    )

    fun assertRemoteRepositoryMapping(
        remote: RemoteGitHubRepository,
        domain: DomainGitHubRepository,
        page: Int,
        language: String,
    ) {
        assertEquals(remote.id, domain.id)
        assertEquals(remote.name, domain.title)
        assertEquals(remote.description, domain.description)
        assertEquals(remote.pullsUrl, domain.pullsUrl)
        assertEquals(remote.stargazersCount, domain.stargazersCount)
        assertEquals(remote.forksCount, domain.forksCount)
        assertEquals(remote.htmlUrl, domain.htmlUrl)
        assertEquals(page, domain.page)
        assertEquals(language, domain.language)
        assertEquals(remote.owner?.login, domain.userName)
        assertEquals(remote.owner?.avatarUrl, domain.avatarUrl)
    }

    fun assertDefaultRemoteRepositoryMapping(
        domain: DomainGitHubRepository,
        page: Int,
        language: String,
    ) {
        assertEquals(-1, domain.id)
        assertEquals("", domain.title)
        assertEquals("", domain.description)
        assertEquals("", domain.pullsUrl)
        assertEquals(0, domain.stargazersCount)
        assertEquals(0, domain.forksCount)
        assertEquals("", domain.htmlUrl)
        assertEquals(page, domain.page)
        assertEquals(language, domain.language)
        assertEquals("", domain.userName)
        assertEquals("", domain.avatarUrl)
    }

    fun assertRemotePullRequestMapping(
        remote: RemotePullRequest,
        domain: DomainGitHubPullRequest,
    ) {
        assertEquals(remote.id, domain.id)
        assertEquals(remote.url, domain.htmlUrl)
        assertEquals(remote.title, domain.title)
        assertEquals(remote.body, domain.body)
        assertEquals(remote.user?.login, domain.userName)
        assertEquals(remote.user?.avatarUrl, domain.avatarUrl)
    }

    fun assertDefaultRemotePullRequestMapping(domain: DomainGitHubPullRequest) {
        assertEquals(-1L, domain.id)
        assertEquals("", domain.htmlUrl)
        assertEquals("", domain.title)
        assertEquals("", domain.body)
        assertEquals("", domain.userName)
        assertEquals("", domain.avatarUrl)
    }
}
