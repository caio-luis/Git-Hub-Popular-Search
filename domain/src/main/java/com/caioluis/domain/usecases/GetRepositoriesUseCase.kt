package com.caioluis.domain.usecases

import com.caioluis.domain.base.BaseUseCase
import com.caioluis.domain.base.Response
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.repository.GitHubReposRepository
import com.sun.org.apache.xpath.internal.operations.Bool

/**
 * Created by Caio Luis (@caio.luis) on 10/10/20
 */
class GetRepositoriesUseCase(
    private val gitHubReposRepository: GitHubReposRepository
) : BaseUseCase<Boolean, List<DomainGitHubRepository>>() {

    private var pageNumber = 1

    private suspend fun loadRepositories(): List<DomainGitHubRepository> {
        return gitHubReposRepository.getGitHubRepositories(pageNumber)
    }

    override suspend fun run(parameters: Boolean?) {
        responseChannel.run {

            send(Response.Loading)

            if (parameters == true){
                pageNumber = 1
            }
            val response = try {
                pageNumber++
                Response.Success(loadRepositories())
            } catch (ex: Exception) {
                Response.Failure(ex)
            }

            send(response)
        }
    }
}