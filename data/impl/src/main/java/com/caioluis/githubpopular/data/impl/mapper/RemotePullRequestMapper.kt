package com.caioluis.githubpopular.data.impl.mapper

import com.caioluis.githubpopular.data.bridge.remote.githubpulls.model.RemotePullRequest
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest

interface RemotePullRequestMapper {
    fun mapToDomain(remotePullRequest: RemotePullRequest): DomainGitHubPullRequest
}
