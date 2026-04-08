package com.caioluis.githubpopular.core.common.exception

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AppExceptionTest {

    @Test
    fun `NetworkException can be created without parameters`() {
        // Arrange & Act
        val exception = AppException.NetworkException()

        // Assert
        assertTrue(exception is AppException)
        assertTrue(exception is Throwable)
    }

    @Test
    fun `NetworkException can have cause`() {
        // Arrange
        val cause = RuntimeException("Root cause")

        // Act
        val exception = AppException.NetworkException(cause = cause)

        // Assert
        assertNotNull(exception.cause)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `TimeoutException can be created without parameters`() {
        // Arrange & Act
        val exception = AppException.TimeoutException()

        // Assert
        assertTrue(exception is AppException)
        assertTrue(exception is Throwable)
    }

    @Test
    fun `TimeoutException can have cause`() {
        // Arrange
        val cause = Exception("Timeout cause")

        // Act
        val exception = AppException.TimeoutException(cause = cause)

        // Assert
        assertNotNull(exception.cause)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `ServerException can be created without parameters`() {
        // Arrange & Act
        val exception = AppException.ServerException()

        // Assert
        assertTrue(exception is AppException)
        assertTrue(exception is Throwable)
    }

    @Test
    fun `ServerException can have cause`() {
        // Arrange
        val cause = Exception("Server error cause")

        // Act
        val exception = AppException.ServerException(cause = cause)

        // Assert
        assertNotNull(exception.cause)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `ParsingException can be created without parameters`() {
        // Arrange & Act
        val exception = AppException.ParsingException()

        // Assert
        assertTrue(exception is AppException)
        assertTrue(exception is Throwable)
    }

    @Test
    fun `ParsingException can have cause`() {
        // Arrange
        val cause = Exception("Parsing cause")

        // Act
        val exception = AppException.ParsingException(cause = cause)

        // Assert
        assertNotNull(exception.cause)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `UnknownException can be created without parameters`() {
        // Arrange & Act
        val exception = AppException.UnknownException()

        // Assert
        assertTrue(exception is AppException)
        assertTrue(exception is Throwable)
    }

    @Test
    fun `UnknownException can have cause`() {
        // Arrange
        val cause = Exception("Unknown cause")

        // Act
        val exception = AppException.UnknownException(cause = cause)

        // Assert
        assertNotNull(exception.cause)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `AppException is a Throwable subclass`() {
        // Arrange & Act
        val exception = AppException.NetworkException()

        // Assert
        assertTrue(exception is Throwable)
    }

    @Test
    fun `all AppException types are Throwable instances`() {
        // Arrange & Act
        val networkException = AppException.NetworkException()
        val timeoutException = AppException.TimeoutException()
        val serverException = AppException.ServerException()
        val parsingException = AppException.ParsingException()
        val unknownException = AppException.UnknownException()

        // Assert
        assertTrue(networkException is Throwable)
        assertTrue(timeoutException is Throwable)
        assertTrue(serverException is Throwable)
        assertTrue(parsingException is Throwable)
        assertTrue(unknownException is Throwable)
    }

    @Test
    fun `NetworkException and TimeoutException are different types`() {
        // Arrange
        val networkException = AppException.NetworkException()
        val timeoutException = AppException.TimeoutException()

        // Act & Assert
        assertFalse(networkException::class == timeoutException::class)
        assertTrue(networkException is AppException.NetworkException)
        assertTrue(timeoutException is AppException.TimeoutException)
    }

    @Test
    fun `ServerException with cause preserves cause chain`() {
        // Arrange
        val rootCause = Exception("Root cause")
        val serverException = AppException.ServerException(cause = rootCause)

        // Act & Assert
        assertEquals(rootCause, serverException.cause)
    }

    @Test
    fun `all exception types preserve cause chain`() {
        // Arrange
        val cause = Exception("Common cause")

        // Act
        val networkException = AppException.NetworkException(cause = cause)
        val timeoutException = AppException.TimeoutException(cause = cause)
        val serverException = AppException.ServerException(cause = cause)
        val parsingException = AppException.ParsingException(cause = cause)
        val unknownException = AppException.UnknownException(cause = cause)

        // Assert
        assertEquals(cause, networkException.cause)
        assertEquals(cause, timeoutException.cause)
        assertEquals(cause, serverException.cause)
        assertEquals(cause, parsingException.cause)
        assertEquals(cause, unknownException.cause)
    }

    @Test
    fun `AppException types can be created with null cause`() {
        // Arrange & Act
        val networkException = AppException.NetworkException(cause = null)
        val timeoutException = AppException.TimeoutException(cause = null)
        val serverException = AppException.ServerException(cause = null)
        val parsingException = AppException.ParsingException(cause = null)
        val unknownException = AppException.UnknownException(cause = null)

        // Assert
        assertEquals(null, networkException.cause)
        assertEquals(null, timeoutException.cause)
        assertEquals(null, serverException.cause)
        assertEquals(null, parsingException.cause)
        assertEquals(null, unknownException.cause)
    }
}
