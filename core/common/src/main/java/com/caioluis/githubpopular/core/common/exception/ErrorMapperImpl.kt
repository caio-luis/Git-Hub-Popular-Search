package com.caioluis.githubpopular.core.common.exception

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ErrorMapperImpl @Inject constructor() : ErrorMapper {

    override fun map(throwable: Throwable): AppException = when (throwable) {
        is SocketTimeoutException -> AppException.TimeoutException(cause = throwable)

        is ConnectException -> AppException.NetworkException(cause = throwable)

        is IOException -> AppException.NetworkException(cause = throwable)

        is HttpException -> {
            when (throwable.code()) {
                in 500..599 -> AppException.ServerException(throwable)
                404 -> AppException.ServerException(throwable)
                else -> AppException.ServerException(throwable)
            }
        }

        is SerializationException -> AppException.ParsingException(cause = throwable)

        else -> AppException.UnknownException(cause = throwable)
    }
}
