package com.caioluis.githubpopular.ui

import androidx.compose.ui.graphics.vector.ImageVector

data class ErrorUiState(
    val icon: ImageVector,
    val message: String,
    val actionText: String,
    val contentDescription: String,
)
