package com.caioluis.githubpopular.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.caioluis.githubpopular.Constants
import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.ui.PreviewsData.getPreviewData
import com.caioluis.githubpopular.ui.components.CustomSpinner
import com.caioluis.githubpopular.ui.components.RepositoriesList
import com.caioluis.githubpopular.ui.theme.GitHubPopularTheme
import com.caioluis.githubpopular.viewmodel.GetRepositoriesViewModel
import com.caioluis.githubpopular.viewmodel.MoreReposViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeActivity : ComponentActivity() {

    // TODO("precisa arrumar a lista quando chega no fim que manda multiplos requests (?)")

    private val gitHubRepositoriesViewModel: GetRepositoriesViewModel by viewModel()
    private val moreReposViewModel: MoreReposViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val programmingLanguages = Constants.languages
            var selectedOption by remember { mutableStateOf(programmingLanguages.first()) }

            gitHubRepositoriesViewModel.loadList(selectedOption)

            val data = remember { mutableStateListOf<UiGitHubRepository>() }
            var isLoading by remember { mutableStateOf(false) }
            var error: Throwable? by remember { mutableStateOf(null) }

            val onLoadingCallback = { isLoading = !isLoading }

            gitHubRepositoriesViewModel
                .observeGitHubReposLiveData.observe(this) { repositoriesResponse ->
                    repositoriesResponse.handleResponse(
                        onLoading = onLoadingCallback,
                        onSuccess = {
                            data.clear()
                            data.addAll(it)
                        },
                        onFailure = { error = it }
                    )
                }

            moreReposViewModel.observeMoreReposLiveData.observe(this) { moreRepos ->
                moreRepos.handleResponse(
                    onLoading = onLoadingCallback,
                    onSuccess = {
                        data.addAll(it)
                    },
                    onFailure = { error = it }
                )
            }

            GitHubPopularTheme {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    RepositoriesList(
                        modifier = Modifier.weight(1F),
                        data = data,
                        isLoading = isLoading,
                        error = error,
                        onEndReached = {
                            moreReposViewModel.loadMore(selectedOption)
                        },
                    )
                    CustomSpinner(
                        options = programmingLanguages,
                        selectedOption = selectedOption,
                        onOptionSelected = { option ->
                            selectedOption = option
                            gitHubRepositoriesViewModel.loadList(option)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListActivityPreview() {

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        RepositoriesList(
            modifier = Modifier.weight(1F),
            data = getPreviewData(10),
            onEndReached = {},
            isLoading = false
        )

        CustomSpinner(
            options = listOf("Kotlin, Java"),
            selectedOption = "Kotlin",
            onOptionSelected = {}
        )
    }
}

