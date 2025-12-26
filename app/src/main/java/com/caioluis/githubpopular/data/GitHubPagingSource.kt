package com.caioluis.githubpopular.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.domain.bridge.usecase.GetRepositoriesUseCase

class GitHubPagingSource(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
    private val language: String,
) : PagingSource<Int, DomainGitHubRepository>() {

    override fun getRefreshKey(state: PagingState<Int, DomainGitHubRepository>): Int? = state.anchorPosition?.let { anchorPosition ->
        state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DomainGitHubRepository> {
        val page = params.key ?: 1
        return try {
            val repositories =
                getRepositoriesUseCase.loadRepositories(page = page, language = language)

            LoadResult.Page(
                data = repositories,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (repositories.isEmpty()) null else page + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
