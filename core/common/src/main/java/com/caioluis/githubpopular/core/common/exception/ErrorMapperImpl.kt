package com.caioluis.githubpopular.core.common.exception

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorMapperImpl @Inject constructor() : ErrorMapper {

    override fun map(throwable: Throwable): AppException = when (throwable) {
        is SocketTimeoutException -> AppException.TimeoutException(throwable)
        is ConnectException, is UnknownHostException -> AppException.NetworkException(throwable)
        is IOException -> AppException.NetworkException(throwable)
        is HttpException -> AppException.ServerException(throwable)
        is SerializationException -> AppException.ParsingException(throwable)
        else -> AppException.UnknownException(throwable)
    }
}
