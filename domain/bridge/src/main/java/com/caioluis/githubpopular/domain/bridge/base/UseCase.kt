package com.caioluis.githubpopular.domain.bridge.base

import com.caioluis.githubpopular.domain.bridge.base.InvokeMode.LOCKING
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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
            LOCKING -> runWithLock { run(params) }
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