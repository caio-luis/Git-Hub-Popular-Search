package com.caioluis.githubpopular.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.viewmodel.GitHubRepositoriesViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val gitHubRepositoriesViewModel: GitHubRepositoriesViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gitHubRepositoriesViewModel.fetchRepositories()

        gitHubRepositoriesViewModel
            .observeGitHubReposLiveData.observe(this, Observer {

                it.handleResponse(
                    onLoading = {
                        //todo start loading state on activity
                    },
                    onSuccess = { handleSuccessResponse(repositories = it) },
                    onFailure = {
                        //todo show failure error state on activity
                    }
                )
            })
    }

    private fun handleSuccessResponse(repositories: List<UiGitHubRepository>) {
        // TODO populate the repositories list on Activity
    }
}