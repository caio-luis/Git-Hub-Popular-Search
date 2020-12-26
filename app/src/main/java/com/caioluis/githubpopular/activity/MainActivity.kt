package com.caioluis.githubpopular.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.caioluis.githubpopular.util.PaginationEventFilter
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.adapter.GitHubRepositoriesAdapter
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.viewmodel.GitHubRepositoriesViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val refreshLayout: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.ghSwipeRefreshLayout) }
    private val ghRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.gitHubRepositoriesRecyclerView) }
    private val repositoriesAdapter: GitHubRepositoriesAdapter by lazy { GitHubRepositoriesAdapter() }
    private val gitHubRepositoriesViewModel: GitHubRepositoriesViewModel by inject()

    var isReloadListStarted = true

    private val lastItemOnListThrottler = PaginationEventFilter(
        lifecycleScope,
        ::doOnListEndReached
    )

    private fun doOnListEndReached() {
        isReloadListStarted = false
        loadList(isReloadListStarted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureRecyclerView()
        initOnRefreshListener()
        loadList(isReloadListStarted)
        observeGitHubRepositories()
    }

    private fun configureRecyclerView() {
        ghRecyclerView.adapter = repositoriesAdapter

        ghRecyclerView.addOnScrollListener(
            repositoriesAdapter.addPagination(lastItemOnListThrottler::sendEvent)
        )
    }

    private fun initOnRefreshListener() {
        isReloadListStarted = true
        refreshLayout.setOnRefreshListener { loadList(isReloadListStarted) }
    }

    private fun observeGitHubRepositories() {
        gitHubRepositoriesViewModel
            .observeGitHubReposLiveData.observe(this,
                Observer { repositoriesResponse ->

                    repositoriesResponse.handleResponse(
                        onLoading = ::setSwipeLoadingState,
                        onSuccess = ::handleSuccessResponse,
                        onFailure = { setSwipeLoadedState() }
                    )
                })
    }

    private fun handleSuccessResponse(repositories: List<UiGitHubRepository>) {
        setSwipeLoadedState()
        repositoriesAdapter.populateList(repositories, isReloadListStarted)
    }

    private fun setSwipeLoadingState() {
        if (!refreshLayout.isRefreshing)
            refreshLayout.isRefreshing = true
    }

    private fun setSwipeLoadedState() {
        if (refreshLayout.isRefreshing)
            refreshLayout.isRefreshing = false
    }

    private fun loadList(isReloading: Boolean) {
        gitHubRepositoriesViewModel.loadList(isReloading)
    }
}