package com.caioluis.githubpopular.core.common.exception

/**
 * Sealed class representing application-specific exceptions.
 * Provides granular error handling with specific error types.
 */
sealed class AppException : Throwable() {
    data class NetworkException(
        override val cause: Throwable? = null,
    ) : AppException()

    data class TimeoutException(
        override val cause: Throwable? = null,
    ) : AppException()

    data class ServerException(
        override val cause: Throwable? = null,
    ) : AppException()

    data class ParsingException(
        override val cause: Throwable? = null,
    ) : AppException()

    data class UnknownException(
        override val cause: Throwable? = null,
    ) : AppException()
}
