package com.caioluis.githubpopular.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Wifi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ErrorUiStateTest {

    @Test
    fun `ErrorUiState can be created with all parameters`() {
        // Arrange
        val icon = Icons.Default.Wifi
        val message = "Network error"
        val actionText = "Retry"
        val contentDescription = "Network error icon"

        // Act
        val uiState = ErrorUiState(
            icon = icon,
            message = message,
            actionText = actionText,
            contentDescription = contentDescription,
        )

        // Assert
        assertEquals(icon, uiState.icon)
        assertEquals(message, uiState.message)
        assertEquals(actionText, uiState.actionText)
        assertEquals(contentDescription, uiState.contentDescription)
    }

    @Test
    fun `ErrorUiState with network icon`() {
        // Arrange
        val message = "Network connectivity failed"
        val uiState = ErrorUiState(
            icon = Icons.Default.Wifi,
            message = message,
            actionText = "Retry",
            contentDescription = "Network error",
        )

        // Act & Assert
        assertEquals(Icons.Default.Wifi, uiState.icon)
        assertEquals(message, uiState.message)
    }

    @Test
    fun `ErrorUiState with timeout icon`() {
        // Arrange
        val message = "Request timeout"
        val uiState = ErrorUiState(
            icon = Icons.Default.Schedule,
            message = message,
            actionText = "Try again",
            contentDescription = "Timeout error",
        )

        // Act & Assert
        assertEquals(Icons.Default.Schedule, uiState.icon)
        assertEquals(message, uiState.message)
    }

    @Test
    fun `ErrorUiState with server error icon`() {
        // Arrange
        val message = "Server error occurred"
        val uiState = ErrorUiState(
            icon = Icons.Default.Error,
            message = message,
            actionText = "Reload",
            contentDescription = "Server error",
        )

        // Act & Assert
        assertEquals(Icons.Default.Error, uiState.icon)
        assertEquals(message, uiState.message)
    }

    @Test
    fun `ErrorUiState with empty message`() {
        // Arrange
        val message = ""
        val uiState = ErrorUiState(
            icon = Icons.Default.Error,
            message = message,
            actionText = "Retry",
            contentDescription = "Error",
        )

        // Act & Assert
        assertEquals("", uiState.message)
    }

    @Test
    fun `ErrorUiState with long message`() {
        // Arrange
        val message = "This is a very long error message that explains what went wrong in detail"
        val uiState = ErrorUiState(
            icon = Icons.Default.Error,
            message = message,
            actionText = "Try again",
            contentDescription = "Error",
        )

        // Act & Assert
        assertEquals(message, uiState.message)
    }

    @Test
    fun `ErrorUiState data class properties`() {
        // Arrange
        val uiState1 = ErrorUiState(
            icon = Icons.Default.Wifi,
            message = "Error",
            actionText = "Retry",
            contentDescription = "Icon",
        )
        val uiState2 = ErrorUiState(
            icon = Icons.Default.Wifi,
            message = "Error",
            actionText = "Retry",
            contentDescription = "Icon",
        )

        // Act & Assert - test data class equality
        assertEquals(uiState1, uiState2)
    }

    @Test
    fun `ErrorUiState data class inequality`() {
        // Arrange
        val uiState1 = ErrorUiState(
            icon = Icons.Default.Wifi,
            message = "Network Error",
            actionText = "Retry",
            contentDescription = "Icon",
        )
        val uiState2 = ErrorUiState(
            icon = Icons.Default.Error,
            message = "Server Error",
            actionText = "Retry",
            contentDescription = "Icon",
        )

        // Act & Assert
        assert(uiState1 != uiState2)
    }

    @Test
    fun `ErrorUiState copy method`() {
        // Arrange
        val original = ErrorUiState(
            icon = Icons.Default.Wifi,
            message = "Network error",
            actionText = "Retry",
            contentDescription = "Network icon",
        )

        // Act
        val copy = original.copy(message = "Updated error message")

        // Assert
        assertEquals(original.icon, copy.icon)
        assertEquals("Updated error message", copy.message)
        assertEquals(original.actionText, copy.actionText)
        assertEquals(original.contentDescription, copy.contentDescription)
    }

    @Test
    fun `ErrorUiState with special characters in message`() {
        // Arrange
        val message = "Error! @ # $ % ^ & * ( ) ~ ` { } [ ] | \\ : \" ; ' < > , . ? /"
        val uiState = ErrorUiState(
            icon = Icons.Default.Error,
            message = message,
            actionText = "OK",
            contentDescription = "Error with special chars",
        )

        // Act & Assert
        assertEquals(message, uiState.message)
        assertNotNull(uiState)
    }

    @Test
    fun `ErrorUiState with unicode characters`() {
        // Arrange
        val message = "Erro de rede 🌐 ⚠️"
        val uiState = ErrorUiState(
            icon = Icons.Default.Wifi,
            message = message,
            actionText = "Tentar novamente",
            contentDescription = "Erro de conectividade",
        )

        // Act & Assert
        assertEquals(message, uiState.message)
    }
}
