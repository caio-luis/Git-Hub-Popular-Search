package com.caioluis.domain.base

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

/**
 * Created by Caio Luis (caio-luis) on 12/10/20
 */
abstract class BaseUseCase<in S : Any, T>(private val invokeMode: InvokeMode) : CoroutineScope {

    private val mutex = Mutex()
    private val parentJob = SupervisorJob()
    private val mainDispatcher = Dispatchers.Main
    private val backgroundDispatcher = Dispatchers.Default

    protected val responseChannel = Channel<Response<T>>()
    val receiveChannel: ReceiveChannel<Response<T>> = responseChannel

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    override val coroutineContext: CoroutineContext
        get() = parentJob + mainDispatcher + exceptionHandler

    protected abstract suspend fun run(parameters: S?)

    operator fun invoke(params: S? = null) {
        when (invokeMode) {
            InvokeMode.LOCKING -> runWithLock { run(params) }
            else -> launch(backgroundDispatcher) { run(params) }
        }
    }

    protected fun <T> runAsync(block: suspend () -> T): Deferred<T> = async(parentJob) {
        block()
    }

    protected fun runWithLock(block: suspend () -> Unit) {
        if (mutex.isLocked)
            return
        launch(backgroundDispatcher) {
            mutex.withLock { block() }
        }
    }

    abstract fun onError(throwable: Throwable)

    fun clear() {
        responseChannel.close()
        parentJob.cancel()
    }
}