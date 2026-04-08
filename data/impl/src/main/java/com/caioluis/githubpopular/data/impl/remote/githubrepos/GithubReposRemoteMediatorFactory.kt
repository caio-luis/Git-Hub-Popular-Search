package com.caioluis.githubpopular.data.impl.remote.githubrepos

import dagger.assisted.AssistedFactory

@AssistedFactory
interface GithubReposRemoteMediatorFactory {
    fun create(language: String): GitHubReposRemoteMediator
}
