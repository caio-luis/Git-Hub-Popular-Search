package com.caioluis.githubpopular.core.common

import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

object Fixtures {

    fun createHttpException(statusCode: Int): HttpException {
        val responseBody = "".toResponseBody()
        val mockResponse = Response.error<String>(statusCode, responseBody)
        return HttpException(mockResponse)
    }
}
