package com.caioluis.githubpopular.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.caioluis.githubpopular.navigation.AppNavHost
import com.caioluis.githubpopular.theme.GitHubPopularTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubPopularTheme {
                AppNavHost()
            }
        }
    }
}
