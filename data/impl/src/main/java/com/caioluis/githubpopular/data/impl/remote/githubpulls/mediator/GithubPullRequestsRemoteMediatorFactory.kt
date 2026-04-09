package com.caioluis.githubpopular.data.impl.remote.githubpulls.mediator

import dagger.assisted.AssistedFactory

@AssistedFactory
interface GithubPullRequestsRemoteMediatorFactory {
    fun create(
        pullUrl: String,
        repositoryId: Int,
    ): GitHubPullRequestsRemoteMediator
}
