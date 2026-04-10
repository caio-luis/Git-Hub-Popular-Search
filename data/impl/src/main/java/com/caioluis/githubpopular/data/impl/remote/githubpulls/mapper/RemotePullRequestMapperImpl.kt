package com.caioluis.githubpopular.data.impl.remote.githubpulls.mapper

import com.caioluis.githubpopular.data.bridge.remote.githubpulls.model.RemotePullRequest
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import javax.inject.Inject

class RemotePullRequestMapperImpl @Inject constructor() : RemotePullRequestMapper {
    override fun mapToDomain(remotePullRequest: RemotePullRequest) = DomainGitHubPullRequest(
        id = remotePullRequest.id ?: -1L,
        htmlUrl = remotePullRequest.url.orEmpty(),
        title = remotePullRequest.title.orEmpty(),
        body = remotePullRequest.body.orEmpty(),
        userName = remotePullRequest.user?.login.orEmpty(),
        avatarUrl = remotePullRequest.user?.avatarUrl.orEmpty(),
    )
}
