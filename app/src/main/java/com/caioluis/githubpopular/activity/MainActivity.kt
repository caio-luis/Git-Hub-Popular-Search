package com.caioluis.githubpopular.activity

import android.R.layout
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.caioluis.githubpopular.Constants.languages
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.adapter.EndlessScrollListener
import com.caioluis.githubpopular.adapter.GitHubRepositoriesAdapter
import com.caioluis.githubpopular.databinding.ActivityMainBinding
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.viewmodel.GetRepositoriesViewModel
import com.caioluis.githubpopular.viewmodel.MoreReposViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var binding: ActivityMainBinding

    private val getRepositoriesViewModel: GetRepositoriesViewModel by viewModel()
    private val moreReposViewModel: MoreReposViewModel by viewModel()

    private val endlessScrollListener: EndlessScrollListener by lazy { getEndlessListener() }
    private val repositoriesAdapter: GitHubRepositoriesAdapter by lazy { getAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureRecyclerView()
        initOnRefreshListener()
        setUpSpinner()
        loadList()
        observeGitHubRepositories()
    }

    private fun getAdapter() =
        GitHubRepositoriesAdapter {
            repositoriesAdapter.removeAllRetryButtons()
            moreReposViewModel.loadMore(getSelectedLanguage())
        }

    private fun setUpSpinner() {
        val adapter = ArrayAdapter(this, layout.simple_spinner_item, languages)

        adapter.setDropDownViewResource(layout.simple_spinner_dropdown_item)

        binding.languagesList.adapter = adapter

        binding.languagesList.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    getRepositoriesViewModel.loadList(getSelectedLanguage())
                }

                override fun onNothingSelected(parent: AdapterView<*>) = Unit
            }
    }

    private fun getSelectedLanguage() = binding.languagesList.selectedItem.toString()

    private fun getEndlessListener(): EndlessScrollListener =
        object : EndlessScrollListener(
            layoutManager = binding.gitHubRepositoriesRecyclerView.layoutManager as LinearLayoutManager,
        ) {
            override fun onLoadMoreItems() {
                moreReposViewModel.loadMore(getSelectedLanguage())
            }
        }

    private fun configureRecyclerView() {
        binding.gitHubRepositoriesRecyclerView.adapter = repositoriesAdapter
        binding.gitHubRepositoriesRecyclerView.addOnScrollListener(endlessScrollListener)
    }

    private fun initOnRefreshListener() {
        binding.ghSwipeRefreshLayout.setOnRefreshListener(::loadList)
    }

    private fun loadList() {
        getRepositoriesViewModel.loadList(getSelectedLanguage())
    }

    private fun observeGitHubRepositories() {
        getRepositoriesViewModel
            .observeGitHubReposLiveData
            .observe(this) { repositoriesResponse ->
                repositoriesResponse.handleResponse(
                    onLoading = ::setSwipeLoadingState,
                    onSuccess = ::handleSuccessResponse,
                    onFailure = ::handleFailureResponse,
                )
            }

        moreReposViewModel
            .observeMoreReposLiveData
            .observe(this) { repositoriesResponse ->
                repositoriesResponse.handleResponse(
                    onLoading = { setProgressBarState(show = true) },
                    onSuccess = ::handleLoadMoreSuccessResponse,
                    onFailure = ::handleLoadMoreFailure,
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
        if (!binding.ghSwipeRefreshLayout.isRefreshing) {
            endlessScrollListener.reset()
            binding.ghSwipeRefreshLayout.isRefreshing = true
        }
    }

    private fun setSwipeLoadedState() {
        if (binding.ghSwipeRefreshLayout.isRefreshing) {
            binding.ghSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setProgressBarState(show: Boolean) {
        if (show) {
            binding.loadingMoreProgressBar.visibility = View.VISIBLE
        } else {
            binding.loadingMoreProgressBar.visibility = View.GONE
        }
    }
}
