package com.caioluis.domain.usecases

import com.caioluis.domain.base.InvokeMode
import com.caioluis.domain.base.Response
import com.caioluis.domain.base.UseCase
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.repository.GitHubReposRepository
import com.caioluis.domain.usecases.ActualPage.pageNumber

class GetMoreReposUseCase(
    private val gitHubReposRepository: GitHubReposRepository,
) : UseCase<Int, List<DomainGitHubRepository>>(InvokeMode.LOCKING) {

    private suspend fun loadRepositories(page: Int): List<DomainGitHubRepository>? {
        return gitHubReposRepository.getGitHubRepositories(page = page)
    }

    override suspend fun run(parameters: Int?) {
        sendChannel.run {
            send(Response.Loading)
            loadRepositories(page = pageNumber)
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
