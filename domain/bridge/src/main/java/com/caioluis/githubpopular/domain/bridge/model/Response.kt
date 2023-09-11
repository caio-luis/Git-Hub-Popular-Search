package com.caioluis.githubpopular.domain.bridge.model

sealed class Response<out T> {
    class Failure(val exception: Throwable? = null) : Response<Nothing>()
    class Success<out T>(val successData: T) : Response<T>()
    object Loading : Response<Nothing>()

    fun handleResponse(
        onLoading: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
        onFailure: (Throwable?) -> Unit = {},
    ) {
        when (this) {
            is Loading -> onLoading()
            is Success -> onSuccess(successData)
            is Failure -> onFailure(exception)
        }
    }
}