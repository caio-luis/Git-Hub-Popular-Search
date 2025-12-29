package com.caioluis.githubpopular.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.caioluis.githubpopular.theme.GitHubPopularTheme
import com.caioluis.githubpopular.ui.MainScreen
import com.caioluis.githubpopular.ui.githubpulls.PullRequestsScreen
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubPopularTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            onRepositoryClick = { repository ->
                                val encodedUrl = URLEncoder.encode(
                                    repository.pullsUrl,
                                    StandardCharsets.UTF_8.toString(),
                                )
                                navController.navigate(
                                    "pullRequests/$encodedUrl/${repository.id}/${repository.title}",
                                )
                            },
                        )
                    }
                    composable(
                        route = "pullRequests/{pullUrl}/{repositoryId}/{repositoryName}",
                        arguments = listOf(
                            navArgument("pullUrl") { type = NavType.StringType },
                            navArgument("repositoryId") { type = NavType.IntType },
                            navArgument("repositoryName") { type = NavType.StringType },
                        ),
                    ) { backStackEntry ->
                        val pullUrl = backStackEntry.arguments?.getString("pullUrl") ?: ""
                        val repositoryId = backStackEntry.arguments?.getInt("repositoryId") ?: 0
                        val repositoryName = backStackEntry.arguments?.getString("repositoryName") ?: ""

                        PullRequestsScreen(
                            pullUrl = pullUrl,
                            repositoryId = repositoryId,
                            repositoryName = repositoryName,
                            onBackClick = {
                                navController.popBackStack()
                            },
                        )
                    }
                }
            }
        }
    }
}
