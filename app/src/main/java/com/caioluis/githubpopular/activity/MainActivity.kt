package com.caioluis.githubpopular.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.adapter.EndlessScrollListener
import com.caioluis.githubpopular.adapter.GitHubRepositoriesAdapter
import com.caioluis.githubpopular.extensions.showShortToast
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.viewmodel.GitHubRepositoriesViewModel
import com.caioluis.githubpopular.viewmodel.MoreReposViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Caio Luis (caio-luis) on 12/10/20
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val refreshLayout: SwipeRefreshLayout get() = findViewById(R.id.ghSwipeRefreshLayout)
    private val ghRecyclerView: RecyclerView get() = findViewById(R.id.gitHubRepositoriesRecyclerView)
    private val progressBar: ProgressBar get() = findViewById(R.id.loading_more_progress_bar)
    private val repositoriesAdapter: GitHubRepositoriesAdapter by lazy { GitHubRepositoriesAdapter() }

    private val gitHubRepositoriesViewModel: GitHubRepositoriesViewModel by viewModel()
    private val moreReposViewModel: MoreReposViewModel by viewModel()

    private val endlessScrollListener: EndlessScrollListener by lazy { getEndlessListener() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureRecyclerView()
        initOnRefreshListener()
        loadList()
        observeGitHubRepositories()
    }

    private fun getEndlessListener(): EndlessScrollListener {
        return object : EndlessScrollListener(
            layoutManager = ghRecyclerView.layoutManager as LinearLayoutManager,
            coroutineScope = lifecycleScope
        ) {
            override fun onLoadMoreItems() {
                moreReposViewModel.loadMore()
            }
        }
    }

    private fun configureRecyclerView() {
        val endlessScrollListener = object : EndlessScrollListener(
            layoutManager = ghRecyclerView.layoutManager as LinearLayoutManager,
            coroutineScope = lifecycleScope
        ) {
            override fun onLoadMoreItems() {
                moreReposViewModel.loadMore()
            }
        }

        endlessScrollListener.startListeningToEvents()
        ghRecyclerView.adapter = repositoriesAdapter
        ghRecyclerView.addOnScrollListener(endlessScrollListener)
    }

    private fun initOnRefreshListener() {
        refreshLayout.setOnRefreshListener {
            loadList()
        }
    }

    private fun loadList() {
        gitHubRepositoriesViewModel.loadList()
    }

    private fun observeGitHubRepositories() {
        gitHubRepositoriesViewModel
            .observeGitHubReposLiveData.observe(this) { repositoriesResponse ->
                repositoriesResponse.handleResponse(
                    onLoading = ::setSwipeLoadingState,
                    onSuccess = ::handleSuccessResponse,
                    onFailure = ::handleFailureResponse
                )
            }

        moreReposViewModel
            .observeMoreReposLiveData.observe(this) { repositoriesResponse ->
                repositoriesResponse.handleResponse(
                    onLoading = { setProgressBarState(show = true) },
                    onSuccess = ::handleLoadMoreSuccessResponse,
                    onFailure = ::handleLoadMoreFailure
                )
            }
    }

    private fun handleSuccessResponse(repositories: List<UiGitHubRepository>) {
        setSwipeLoadedState()
        repositoriesAdapter.refreshList(repositories)
    }

    private fun handleFailureResponse(error: Throwable?) {
        setSwipeLoadedState()
        showShortToast(error?.message.toString())
    }

    private fun handleLoadMoreSuccessResponse(repositories: List<UiGitHubRepository>) {
        setProgressBarState(show = false)
        repositoriesAdapter.insertMoreItems(repositories)
    }

    private fun handleLoadMoreFailure(error: Throwable?) {
        setProgressBarState(show = false)
        showShortToast(error?.message.toString())
    }

    private fun setSwipeLoadingState() {
        if (!refreshLayout.isRefreshing)
            refreshLayout.isRefreshing = true
    }

    private fun setSwipeLoadedState() {
        if (refreshLayout.isRefreshing)
            refreshLayout.isRefreshing = false
    }

    private fun setProgressBarState(show: Boolean) {
        if (show)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        endlessScrollListener.dispose()
        super.onDestroy()
    }
}
