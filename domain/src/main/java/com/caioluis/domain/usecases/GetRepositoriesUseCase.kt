package com.caioluis.domain.usecases

import com.caioluis.domain.base.BaseUseCase
import com.caioluis.domain.base.Response
import com.caioluis.domain.entity.DomainGitHubRepository
import com.caioluis.domain.repository.GitHubReposRepository

/**
 * Created by Caio Luis (@caio.luis) on 10/10/20
 */
class GetRepositoriesUseCase(
    private val gitHubReposRepository: GitHubReposRepository
) : BaseUseCase<Int, List<DomainGitHubRepository>>() {
    override suspend fun run(parameters: Int) {

        responseChannel.run {

            send(Response.Loading)

            val response = try {
                Response.Success(
                    gitHubReposRepository.getGitHubRepositories(parameters)
                )
            } catch (ex: Exception) {
                Response.Failure(ex)
            }

            send(response)
        }
    }
}