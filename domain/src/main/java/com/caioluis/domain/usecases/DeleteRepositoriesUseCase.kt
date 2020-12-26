package com.caioluis.domain.usecases

import com.caioluis.domain.base.BaseUseCase
import com.caioluis.domain.base.Response
import com.caioluis.domain.repository.GitHubReposRepository

/**
 * Created by Caio Luis (@caio.luis) on 20/11/20
 */
class DeleteRepositoriesUseCase(
    private val gitHubReposRepository: GitHubReposRepository
) : BaseUseCase<Int, Unit>() {
    override suspend fun run(parameters: Int?) {

        responseChannel.run {

            send(Response.Loading)

            val response = try {
                Response.Success(
                    gitHubReposRepository.deleteRepositories()
                )
            } catch (ex: Exception) {
                Response.Failure(ex)
            }

            send(response)
        }
    }
}