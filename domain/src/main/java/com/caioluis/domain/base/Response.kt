package com.caioluis.domain.base

/**
 * Created by Caio Luis (caio-luis) on 12/10/20
 */

sealed class Response<out T> {
    class Failure(val exception: Throwable) : Response<Nothing>()
    class Success<out T>(val successData: T) : Response<T>()
    object Loading : Response<Nothing>()

    fun handleResponse(
        onLoading: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
        onFailure: (Throwable) -> Unit = {}
    ) {
        when (this) {
            is Loading -> onLoading()
            is Success -> onSuccess(successData)
            is Failure -> onFailure(exception)
        }
    }
}