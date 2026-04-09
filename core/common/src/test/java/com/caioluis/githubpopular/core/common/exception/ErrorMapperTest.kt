package com.caioluis.githubpopular.core.common.exception

import com.caioluis.githubpopular.core.common.Fixtures
import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorMapperTest {

    private lateinit var errorMapper: ErrorMapper

    @Before
    fun setUp() {
        errorMapper = ErrorMapperImpl()
    }

    @Test
    fun `map SocketTimeoutException to TimeoutException`() {
        val throwable = SocketTimeoutException("Timeout occurred")
        val result = errorMapper.map(throwable)
        assertTrue(result is AppException.TimeoutException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `map ConnectException to NetworkException`() {
        val throwable = ConnectException("Connection refused")
        val result = errorMapper.map(throwable)
        assertTrue(result is AppException.NetworkException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `map UnknownHostException to NetworkException`() {
        val throwable = UnknownHostException("Unknown host")
        val result = errorMapper.map(throwable)
        assertTrue(result is AppException.NetworkException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `map IOException to NetworkException`() {
        val throwable = IOException("IO error")
        val result = errorMapper.map(throwable)
        assertTrue(result is AppException.NetworkException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `all http error codes map to ServerException`() {
        val statusCodes = listOf(400, 401, 403, 404, 500, 502, 503, 599)
        for (statusCode in statusCodes) {
            val httpException = Fixtures.createHttpException(statusCode)
            val result = errorMapper.map(httpException)
            assertTrue(result is AppException.ServerException)
        }
    }

    @Test
    fun `map SerializationException to ParsingException`() {
        val throwable = SerializationException("Invalid JSON")
        val result = errorMapper.map(throwable)
        assertTrue(result is AppException.ParsingException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `map unknown exception to UnknownException`() {
        val throwable = RuntimeException("Some unknown error")
        val result = errorMapper.map(throwable)
        assertTrue(result is AppException.UnknownException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `map null pointer exception to UnknownException`() {
        val throwable = NullPointerException("Null pointer")
        val result = errorMapper.map(throwable)
        assertTrue(result is AppException.UnknownException)
        assertNotNull(result.cause)
    }

    @Test
    fun `map IllegalArgumentException to UnknownException`() {
        val throwable = IllegalArgumentException("Invalid argument")
        val result = errorMapper.map(throwable)
        assertTrue(result is AppException.UnknownException)
        assertEquals(throwable, result.cause)
    }

    @Test
    fun `map AppException should return same instance`() {
        val appException = AppException.NetworkException(Throwable("network"))

        val result = errorMapper.map(appException)

        assertEquals(appException, result)
    }
}
