package com.caioluis.githubpopular.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.caioluis.githubpopular.theme.GitHubPopularTheme
import com.caioluis.githubpopular.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubPopularTheme {
                MainScreen()
            }
        }
    }
}
