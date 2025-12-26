package com.caioluis.githubpopular.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ErrorContent(
    error: Throwable?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            maxLines = 2,
            text = "Error: ${error?.message ?: "Unknown error"}",
        )
        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = onRetry,
        ) {
            Text(text = "Retry")
        }
    }
}

@Preview
@Composable
fun ErrorContentPreview() {
    ErrorContent(error = Exception("An error occurred"), onRetry = {})
}
