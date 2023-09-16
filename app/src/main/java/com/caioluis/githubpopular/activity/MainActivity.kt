package com.caioluis.githubpopular.activity

import android.R.layout
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.caioluis.githubpopular.Constants.languages
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.adapter.EndlessScrollListener
import com.caioluis.githubpopular.adapter.GitHubRepositoriesAdapter
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.viewmodel.GetRepositoriesViewModel
import com.caioluis.githubpopular.viewmodel.MoreReposViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val refreshLayout: SwipeRefreshLayout get() = findViewById(R.id.ghSwipeRefreshLayout)
    private val ghRecyclerView: RecyclerView get() = findViewById(R.id.gitHubRepositoriesRecyclerView)
    private val progressBar: ProgressBar get() = findViewById(R.id.loading_more_progress_bar)
    private val languagesList: Spinner get() = findViewById(R.id.languages_list)

    private val getRepositoriesViewModel: GetRepositoriesViewModel by viewModel()
    private val moreReposViewModel: MoreReposViewModel by viewModel()

    private val endlessScrollListener: EndlessScrollListener by lazy { getEndlessListener() }
    private val repositoriesAdapter: GitHubRepositoriesAdapter by lazy { getAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureRecyclerView()
        initOnRefreshListener()
        setUpSpinner()
        loadList()
        observeGitHubRepositories()
    }

    private fun getAdapter() = GitHubRepositoriesAdapter {
        repositoriesAdapter.removeAllRetryButtons()
        moreReposViewModel.loadMore(getSelectedLanguage())
    }

    private fun setUpSpinner() {
        val adapter = ArrayAdapter(this, layout.simple_spinner_item, languages)

        adapter.setDropDownViewResource(layout.simple_spinner_dropdown_item)

        languagesList.adapter = adapter

        languagesList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                getRepositoriesViewModel.loadList(getSelectedLanguage())
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
    }

    private fun getSelectedLanguage() = languagesList.selectedItem.toString()

    private fun getEndlessListener(): EndlessScrollListener {
        return object : EndlessScrollListener(
            layoutManager = ghRecyclerView.layoutManager as LinearLayoutManager,
        ) {
            override fun onLoadMoreItems() {
                moreReposViewModel.loadMore(getSelectedLanguage())
            }
        }
    }

    private fun configureRecyclerView() {
        ghRecyclerView.adapter = repositoriesAdapter
        ghRecyclerView.addOnScrollListener(endlessScrollListener)
    }

    private fun initOnRefreshListener() {
        refreshLayout.setOnRefreshListener(::loadList)
    }

    private fun loadList() {
        getRepositoriesViewModel.loadList(getSelectedLanguage())
    }

    private fun observeGitHubRepositories() {
        getRepositoriesViewModel
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
        repositoriesAdapter.removeAllRetryButtons()
        repositoriesAdapter.submitList(repositories)
    }

    private fun handleFailureResponse(error: Throwable?) {
        repositoriesAdapter.addRetryButton(exception = error)
        setSwipeLoadedState()
    }

    private fun handleLoadMoreSuccessResponse(repositories: List<UiGitHubRepository>) {
        setProgressBarState(show = false)
        repositoriesAdapter.removeAllRetryButtons()
        repositoriesAdapter.insertMoreItems(repositories)
    }

    private fun handleLoadMoreFailure(error: Throwable?) {
        repositoriesAdapter.addRetryButton(error)
        setProgressBarState(show = false)
    }

    private fun setSwipeLoadingState() {
        if (!refreshLayout.isRefreshing) {
            endlessScrollListener.reset()
            refreshLayout.isRefreshing = true
        }
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
}
