package com.caioluis.githubpopular.data.impl.remote.githubpulls

import dagger.assisted.AssistedFactory

@AssistedFactory
interface GithubPullRequestsRemoteMediatorFactory {
    fun create(
        pullUrl: String,
        repositoryId: Int,
    ): com.caioluis.githubpopular.data.impl.remote.githubpulls.GitHubPullRequestsRemoteMediator
}
