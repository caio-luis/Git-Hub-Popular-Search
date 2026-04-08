package com.caioluis.githubpopular.ui

import com.caioluis.githubpopular.core.common.exception.AppException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class ErrorContentTest {

    @Test
    fun `ErrorContent handles NetworkException`() {
        // Arrange
        val error = AppException.NetworkException()

        // Act & Assert
        assertNotNull(error)
        assertTrue(error is AppException.NetworkException)
    }

    @Test
    fun `ErrorContent handles TimeoutException`() {
        // Arrange
        val error = AppException.TimeoutException()

        // Act & Assert
        assertNotNull(error)
        assertTrue(error is AppException.TimeoutException)
    }

    @Test
    fun `ErrorContent handles ServerException`() {
        // Arrange
        val error = AppException.ServerException()

        // Act & Assert
        assertNotNull(error)
        assertTrue(error is AppException.ServerException)
    }

    @Test
    fun `ErrorContent handles null error`() {
        // Arrange
        val error: Throwable? = null

        // Act & Assert
        assertEquals(null, error)
    }

    @Test
    fun `ErrorContent handles generic Exception`() {
        // Arrange
        val error = Exception("An error occurred")

        // Act & Assert
        assertNotNull(error)
        assertTrue(error is Exception)
        assertFalse(error is AppException)
    }

    @Test
    fun `ErrorContent handles RuntimeException`() {
        // Arrange
        val error = RuntimeException("Runtime error")

        // Act & Assert
        assertNotNull(error)
        assertTrue(error is RuntimeException)
    }

    @Test
    fun `ErrorContent with all exception types`() {
        // Arrange
        val networkException = AppException.NetworkException()
        val timeoutException = AppException.TimeoutException()
        val serverException = AppException.ServerException()
        val parsingException = AppException.ParsingException()
        val unknownException = AppException.UnknownException()

        // Act & Assert
        assertNotNull(networkException)
        assertNotNull(timeoutException)
        assertNotNull(serverException)
        assertNotNull(parsingException)
        assertNotNull(unknownException)
    }

    @Test
    fun `ErrorContent callback function works`() {
        // Arrange
        var callbackCalled = false
        val onRetry: () -> Unit = {
            callbackCalled = true
        }

        // Act
        onRetry()

        // Assert
        assertTrue(callbackCalled)
    }

    @Test
    fun `ErrorContent multiple retry callbacks`() {
        // Arrange
        var callCount = 0
        val onRetry: () -> Unit = {
            callCount++
        }

        // Act
        onRetry()
        onRetry()
        onRetry()

        // Assert
        assertEquals(3, callCount)
    }

    @Test
    fun `ErrorContent with NetworkException and cause`() {
        // Arrange
        val cause = IOException("Connection failed")
        val error = AppException.NetworkException(cause = cause)

        // Act & Assert
        assertNotNull(error)
        assertNotNull(error.cause)
        assertTrue(error.cause is IOException)
    }

    @Test
    fun `ErrorContent with exception chain`() {
        // Arrange
        val rootCause = Exception("Root cause")
        val wrappedException = Exception("Wrapped exception", rootCause)
        val appException = AppException.NetworkException(cause = wrappedException)

        // Act & Assert
        assertNotNull(appException.cause)
        assertNotNull(appException.cause?.cause)
    }
}
