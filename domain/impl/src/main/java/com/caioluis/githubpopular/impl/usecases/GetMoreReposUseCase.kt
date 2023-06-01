package com.caioluis.githubpopular.impl.usecases

import com.caioluis.githubpopular.domain.bridge.base.InvokeMode
import com.caioluis.githubpopular.domain.bridge.base.Response
import com.caioluis.githubpopular.domain.bridge.base.UseCase
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.repository.GitHubReposRepository
import com.caioluis.githubpopular.impl.usecases.ActualPage.pageNumber

class GetMoreReposUseCase(
    private val gitHubReposRepository: GitHubReposRepository,
) : UseCase<String, List<DomainGitHubRepository>>(InvokeMode.LOCKING) {

    private suspend fun loadRepositories(
        page: Int,
        language: String,
    ): List<DomainGitHubRepository>? {
        return gitHubReposRepository.getGitHubRepositories(page = page, language)
    }

    override suspend fun run(parameters: String?) {
        sendChannel.run {
            send(Response.Loading)
            loadRepositories(page = pageNumber, parameters.orEmpty())
                ?.let {
                    pageNumber++
                    send(Response.Success(it))
                }
        }
    }

    override fun onError(throwable: Throwable) {
        runWithLock { sendChannel.send(Response.Failure(throwable)) }
    }
}
