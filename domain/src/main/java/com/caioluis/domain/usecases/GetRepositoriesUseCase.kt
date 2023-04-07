package com.caioluis.domain.usecases

import com.caioluis.domain.base.InvokeMode
import com.caioluis.domain.base.Response
import com.caioluis.domain.base.UseCase
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.repository.GitHubReposRepository
import com.caioluis.domain.usecases.ActualPage.pageNumber

class GetRepositoriesUseCase(
    private val gitHubReposRepository: GitHubReposRepository,
) : UseCase<String, List<DomainGitHubRepository>>(InvokeMode.LAUNCH) {

    private suspend fun loadRepositories(
        page: Int,
        language: String,
    ): List<DomainGitHubRepository>? {
        return gitHubReposRepository.getGitHubRepositories(page = page, language)
    }

    override suspend fun run(parameters: String?) {
        sendChannel.run {
            pageNumber = 1
            send(Response.Loading)
            loadRepositories(page = pageNumber, language = parameters.orEmpty())
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
