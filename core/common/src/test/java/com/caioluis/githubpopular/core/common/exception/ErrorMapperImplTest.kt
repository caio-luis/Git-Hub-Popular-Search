package com.caioluis.githubpopular.core.common.exception

import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

class ErrorMapperImplTest {

    private lateinit var errorMapper: ErrorMapperImpl

    @Before
    fun setUp() {
        errorMapper = ErrorMapperImpl()
    }

    @Test
    fun `map SocketTimeoutException to TimeoutException`() {
        // Arrange
        val throwable = SocketTimeoutException("Timeout occurred")

        // Act
        val result = errorMapper.map(throwable)

        // Assert
        assert(result is AppException.TimeoutException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `map ConnectException to NetworkException`() {
        // Arrange
        val throwable = ConnectException("Connection refused")

        // Act
        val result = errorMapper.map(throwable)

        // Assert
        assert(result is AppException.NetworkException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `map IOException to NetworkException`() {
        // Arrange
        val throwable = IOException("IO error")

        // Act
        val result = errorMapper.map(throwable)

        // Assert
        assert(result is AppException.NetworkException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `map HttpException with 500 status code to ServerException`() {
        // Arrange
        val httpException = createHttpException(500)

        // Act
        val result = errorMapper.map(httpException)

        // Assert
        assert(result is AppException.ServerException)
        assertEquals(httpException, result.cause)
    }

    @Test
    fun `map HttpException with 502 status code to ServerException`() {
        // Arrange
        val httpException = createHttpException(502)

        // Act
        val result = errorMapper.map(httpException)

        // Assert
        assert(result is AppException.ServerException)
        assertEquals(httpException, result.cause)
    }

    @Test
    fun `map HttpException with 599 status code to ServerException`() {
        // Arrange
        val httpException = createHttpException(599)

        // Act
        val result = errorMapper.map(httpException)

        // Assert
        assert(result is AppException.ServerException)
        assertEquals(httpException, result.cause)
    }

    @Test
    fun `map HttpException with 404 status code to ServerException`() {
        // Arrange
        val httpException = createHttpException(404)

        // Act
        val result = errorMapper.map(httpException)

        // Assert
        assert(result is AppException.ServerException)
        assertEquals(httpException, result.cause)
    }

    @Test
    fun `map HttpException with 400 status code to ServerException`() {
        // Arrange
        val httpException = createHttpException(400)

        // Act
        val result = errorMapper.map(httpException)

        // Assert
        assert(result is AppException.ServerException)
        assertEquals(httpException, result.cause)
    }

    @Test
    fun `map HttpException with 403 status code to ServerException`() {
        // Arrange
        val httpException = createHttpException(403)

        // Act
        val result = errorMapper.map(httpException)

        // Assert
        assert(result is AppException.ServerException)
        assertEquals(httpException, result.cause)
    }

    @Test
    fun `map HttpException with 401 status code to ServerException`() {
        // Arrange
        val httpException = createHttpException(401)

        // Act
        val result = errorMapper.map(httpException)

        // Assert
        assert(result is AppException.ServerException)
        assertEquals(httpException, result.cause)
    }

    @Test
    fun `map SerializationException to ParsingException`() {
        // Arrange
        val throwable = SerializationException("Invalid JSON")

        // Act
        val result = errorMapper.map(throwable)

        // Assert
        assert(result is AppException.ParsingException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `map unknown exception to UnknownException`() {
        // Arrange
        val throwable = RuntimeException("Some unknown error")

        // Act
        val result = errorMapper.map(throwable)

        // Assert
        assert(result is AppException.UnknownException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `map null pointer exception to UnknownException`() {
        // Arrange
        val throwable = NullPointerException("Null pointer")

        // Act
        val result = errorMapper.map(throwable)

        // Assert
        assert(result is AppException.UnknownException)
        assertNotNull(result.cause)
    }

    @Test
    fun `map IllegalArgumentException to UnknownException`() {
        // Arrange
        val throwable = IllegalArgumentException("Invalid argument")

        // Act
        val result = errorMapper.map(throwable)

        // Assert
        assert(result is AppException.UnknownException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `all http error codes map to ServerException`() {
        // Arrange
        val statusCodes = listOf(400, 401, 403, 404, 500, 502, 503)

        // Act & Assert
        for (statusCode in statusCodes) {
            val httpException = createHttpException(statusCode)
            val result = errorMapper.map(httpException)
            assert(result is AppException.ServerException)
        }
    }

    @Test
    fun `different network exceptions map correctly`() {
        // Arrange
        val connectException = ConnectException("Connection failed")
        val ioException = IOException("IO failed")
        val timeoutException = SocketTimeoutException("Timeout")

        // Act
        val connectResult = errorMapper.map(connectException)
        val ioResult = errorMapper.map(ioException)
        val timeoutResult = errorMapper.map(timeoutException)

        // Assert
        assert(connectResult is AppException.NetworkException)
        assert(ioResult is AppException.NetworkException)
        assert(timeoutResult is AppException.TimeoutException)
    }

    // Helper function to create HttpException
    private fun createHttpException(statusCode: Int): HttpException {
        val responseBody = "".toResponseBody()
        val mockResponse = Response.error<String>(statusCode, responseBody)
        return HttpException(mockResponse)
    }
}
