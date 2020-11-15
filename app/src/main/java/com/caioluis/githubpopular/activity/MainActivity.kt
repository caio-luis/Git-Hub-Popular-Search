package com.caioluis.githubpopular.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.adapter.GitHubRepositoriesAdapter
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.viewmodel.GitHubRepositoriesViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val gitHubRepositoriesViewModel: GitHubRepositoriesViewModel by inject()

    private val recyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.gitHubRepositoriesRecyclerView) }
    private val refreshLayout: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.ghSwipeRefreshLayout) }

    private val repositoriesAdapter: GitHubRepositoriesAdapter by lazy { GitHubRepositoriesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerView.adapter = repositoriesAdapter
        initOnRefreshListener()
        loadList()
        observeGitHubRepositories()
    }

    private fun initOnRefreshListener() {
        refreshLayout.setOnRefreshListener {
            loadList()
        }
    }

    private fun observeGitHubRepositories() {
        gitHubRepositoriesViewModel
            .observeGitHubReposLiveData.observe(this, Observer { repositoriesResponse ->

                repositoriesResponse.handleResponse(
                    onLoading = ::setLoadingState,
                    onSuccess = ::handleSuccessResponse,
                    onFailure = {
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                        setLoadedState()
                    }
                )
            })
    }

    private fun handleSuccessResponse(repositories: List<UiGitHubRepository>) {
        setLoadedState()
        repositoriesAdapter.updateList(repositories)
    }

    private fun setLoadingState() {
        recyclerView.visibility = View.GONE
        refreshLayout.isRefreshing = true
    }

    private fun setLoadedState() {
        refreshLayout.isRefreshing = false
        recyclerView.visibility = View.VISIBLE
    }

    private fun loadList() {
        SwipeRefreshLayout.OnRefreshListener { gitHubRepositoriesViewModel.fetchRepositories() }.onRefresh()
    }
}