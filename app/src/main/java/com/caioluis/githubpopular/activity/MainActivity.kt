package com.caioluis.githubpopular.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.adapter.GitHubRepositoriesAdapter
import com.caioluis.githubpopular.extensions.showShortToast
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.util.PaginationEventFilter
import com.caioluis.githubpopular.viewmodel.GitHubRepositoriesViewModel
import org.koin.android.ext.android.inject

/**
 * Created by Caio Luis (caio-luis) on 12/10/20
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val refreshLayout: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.ghSwipeRefreshLayout) }
    private val ghRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.gitHubRepositoriesRecyclerView) }
    private val repositoriesAdapter: GitHubRepositoriesAdapter by lazy { GitHubRepositoriesAdapter() }
    private val gitHubRepositoriesViewModel: GitHubRepositoriesViewModel by inject()
    private var hadLoadingProblem = false
    private var isReloadingList = true

    private val paginationEvent = PaginationEventFilter(
        lifecycleScope,
        ::doOnListEndReached
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureRecyclerView()
        initOnRefreshListener()
        loadList(isReloadingList)
        observeGitHubRepositories()
    }

    private fun configureRecyclerView() {
        ghRecyclerView.adapter = repositoriesAdapter

        ghRecyclerView.addOnScrollListener(
            repositoriesAdapter.addPagination {
                paginationEvent.sendEvent(it, hadLoadingProblem)
            }
        )
    }

    private fun initOnRefreshListener() {
        refreshLayout.setOnRefreshListener {
            isReloadingList = true
            loadList(isReloadingList)
            paginationEvent.resetChannel()
        }
    }

    private fun loadList(isReloading: Boolean) {
        gitHubRepositoriesViewModel.loadList(isReloading)
    }

    private fun observeGitHubRepositories() {
        gitHubRepositoriesViewModel
            .observeGitHubReposLiveData.observe(this,
                Observer { repositoriesResponse ->
                    repositoriesResponse.handleResponse(
                        onLoading = ::setSwipeLoadingState,
                        onSuccess = ::handleSuccessResponse,
                        onFailure = ::handleFailureResponse
                    )
                })
    }

    private fun handleSuccessResponse(repositories: List<UiGitHubRepository>) {
        hadLoadingProblem = false
        setSwipeLoadedState()
        repositoriesAdapter.populateList(repositories, isReloadingList)
    }

    private fun handleFailureResponse(error: Throwable) {
        showShortToast(error.message.toString())
        hadLoadingProblem = true
        setSwipeLoadedState()
    }

    private fun doOnListEndReached() {
        isReloadingList = false
        loadList(isReloadingList)
    }

    private fun setSwipeLoadingState() {
        if (!refreshLayout.isRefreshing)
            refreshLayout.isRefreshing = true
    }

    private fun setSwipeLoadedState() {
        if (refreshLayout.isRefreshing)
            refreshLayout.isRefreshing = false
    }
}
