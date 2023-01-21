package com.caioluis.domain.base

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

/**
 * Created by Caio Luis (caio-luis) on 12/10/20
 */
abstract class UseCase<in S : Any, T>(private val invokeMode: InvokeMode) : CoroutineScope {

    private val coroutineName = CoroutineName(this::class.java.name)

    private val mutex = Mutex()
    private val parentJob = SupervisorJob()
    private val mainDispatcher = Dispatchers.Main
    private val backgroundDispatcher = Dispatchers.Default

    private val channel = Channel<Response<T>>(1)

    protected val sendChannel: SendChannel<Response<T>> = channel
    val receiveChannel: ReceiveChannel<Response<T>> = channel

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    override val coroutineContext: CoroutineContext
        get() = parentJob + mainDispatcher + exceptionHandler + coroutineName

    protected abstract suspend fun run(parameters: S?)

    operator fun invoke(params: S? = null) {
        when (invokeMode) {
            InvokeMode.LOCKING -> runWithLock { run(params) }
            else -> launch(backgroundDispatcher) { run(params) }
        }
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
        parentJob.cancel()
        channel.close()
    }
}