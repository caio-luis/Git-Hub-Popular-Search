package com.caioluis.githubpopular.core.common.exception

sealed class AppException(cause: Throwable) : Exception(cause) {

    override fun fillInStackTrace(): Throwable = this

    data class NetworkException(val error: Throwable) : AppException(error)

    data class TimeoutException(val error: Throwable) : AppException(error)

    data class ServerException(val error: Throwable) : AppException(error)

    data class ParsingException(val error: Throwable) : AppException(error)

    data class UnknownException(val error: Throwable) : AppException(error)
}
