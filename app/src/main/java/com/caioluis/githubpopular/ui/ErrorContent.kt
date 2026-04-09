package com.caioluis.githubpopular.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.caioluis.githubpopular.R
import com.caioluis.githubpopular.core.common.exception.AppException

@Composable
fun ErrorContent(
    error: AppException,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState = when (error) {
        is AppException.NetworkException -> ErrorUiState(
            icon = Icons.Default.WifiOff,
            message = stringResource(R.string.error_network_message),
            actionText = stringResource(R.string.error_retry_action),
            contentDescription = stringResource(R.string.error_icon_network),
        )

        is AppException.TimeoutException -> ErrorUiState(
            icon = Icons.Default.Schedule,
            message = stringResource(R.string.error_timeout_message),
            actionText = stringResource(R.string.error_retry_action),
            contentDescription = stringResource(R.string.error_icon_timeout),
        )

        is AppException.ServerException -> ErrorUiState(
            icon = Icons.Default.CloudOff,
            message = stringResource(R.string.error_server_message),
            actionText = stringResource(R.string.error_retry_action),
            contentDescription = stringResource(R.string.error_icon_server),
        )

        is AppException.ParsingException -> ErrorUiState(
            icon = Icons.Default.Error,
            message = stringResource(R.string.error_parsing_message),
            actionText = stringResource(R.string.error_retry_action),
            contentDescription = stringResource(R.string.error_icon_parsing),
        )

        else -> ErrorUiState(
            icon = Icons.Default.Error,
            message = stringResource(R.string.error_unknown_message),
            actionText = stringResource(R.string.error_retry_action),
            contentDescription = stringResource(R.string.error_icon_unknown),
        )
    }

    AnimatedVisibility(visible = true) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(uiState.icon, contentDescription = uiState.contentDescription)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                maxLines = 2,
                text = uiState.message,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
            ) {
                Text(text = uiState.actionText)
            }
        }
    }
}

@Preview
@Composable
fun ErrorContentPreviewNetwork() {
    ErrorContent(
        error = AppException.NetworkException(Throwable("Preview network error")),
        onRetry = {},
    )
}

@Preview
@Composable
fun ErrorContentPreviewTimeout() {
    ErrorContent(
        error = AppException.TimeoutException(Throwable("Preview timeout error")),
        onRetry = {},
    )
}

@Preview
@Composable
fun ErrorContentPreviewServer() {
    ErrorContent(
        error = AppException.ServerException(Throwable("Preview server error")),
        onRetry = {},
    )
}

@Preview
@Composable
fun ErrorContentPreviewUnknown() {
    ErrorContent(
        error = AppException.UnknownException(Throwable("Preview unknown error")),
        onRetry = {},
    )
}
