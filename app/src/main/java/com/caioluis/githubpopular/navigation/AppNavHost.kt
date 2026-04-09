package com.caioluis.githubpopular.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caioluis.githubpopular.githubpulls.ui.PullRequestsScreen
import com.caioluis.githubpopular.ui.MainScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainDestination) {
        composable<MainDestination> {
            MainScreen(
                onRepositoryClick = { repository ->
                    navController.navigate(
                        PullRequestsDestination(
                            pullUrl = repository.pullsUrl,
                            repositoryId = repository.id,
                            repositoryName = repository.title,
                        ),
                    )
                },
            )
        }
        composable<PullRequestsDestination> {
            PullRequestsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
