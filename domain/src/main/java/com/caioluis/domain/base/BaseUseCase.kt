package com.caioluis.domain.base

import kotlinx.coroutines.*;
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

abstract class BaseUseCase<in S : Any, T> : CoroutineScope {

    private val parentJob = SupervisorJob()
    private val mainDispatcher = Dispatchers.Main
    private val backgroundDispatcher = Dispatchers.Default

    protected val responseChannel = Channel<Response<T>>()
    val receiveChannel: ReceiveChannel<Response<T>> = responseChannel

    override val coroutineContext: CoroutineContext
        get() = parentJob + mainDispatcher

    protected abstract suspend fun run(parameters: S?)

    operator fun invoke(params: S? = null) {
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