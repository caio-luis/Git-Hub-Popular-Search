package com.caioluis.githubpopular.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.caioluis.githubpopular.core.common.utils.LogUtil
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubPullRequest
import com.caioluis.githubpopular.domain.bridge.usecase.GetPullRequestsUseCase

class GitHubPullRequestsPagingSource(
    private val getPullRequestsUseCase: GetPullRequestsUseCase,
    private val pullUrl: String,
    private val repositoryId: Int,
) : PagingSource<Int, DomainGitHubPullRequest>() {

    override fun getRefreshKey(state: PagingState<Int, DomainGitHubPullRequest>): Int? = state.anchorPosition?.let { anchorPosition ->
        state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DomainGitHubPullRequest> {
        val page = params.key ?: 1
        return try {
            val pullRequests =
                getPullRequestsUseCase.loadPullRequests(
                    page = page,
                    pullUrl = pullUrl,
                    repositoryId = repositoryId,
                )

            LoadResult.Page(
                data = pullRequests,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (pullRequests.isEmpty()) null else page + 1,
            )
        } catch (e: Exception) {
            LogUtil.e(
                tag = GitHubPullRequestsPagingSource::class.simpleName,
                message = e.message,
                throwable = e,
            )
            LoadResult.Error(e)
        }
    }
}
