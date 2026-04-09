package com.caioluis.githubpopular.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class ErrorUiState(
    val icon: ImageVector,
    val message: String,
    val actionText: String,
    val contentDescription: String,
)
