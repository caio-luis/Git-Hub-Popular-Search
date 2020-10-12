package com.caioluis.domain.base

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

abstract class BaseUseCase<in Parameters : Any, ResponseData> : CoroutineScope {

    private val parentJob = SupervisorJob()
    private val mainDispatcher = Dispatchers.Main
    private val backgroundDispatcher = Dispatchers.Default

    protected val responseChannel = Channel<Response<ResponseData>>()
    val receiveChannel: ReceiveChannel<Response<ResponseData>> = responseChannel

    override val coroutineContext: CoroutineContext
        get() = parentJob + mainDispatcher

    protected abstract suspend fun run(parameters: Parameters)

    operator fun invoke(params: Parameters) {
        launch(backgroundDispatcher) {
            run(params)
        }
    }

    protected fun <T> runAsync(block: suspend () -> T): Deferred<T> = async(parentJob) {
        block()
    }

    fun clear() {
        responseChannel.close()
        parentJob.cancel()
    }
}