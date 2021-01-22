package com.caioluis.githubpopular.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Created by Caio Luis (caio-luis) on 26/12/20
 */

class PaginationEventFilter(private val scope: CoroutineScope, val onConsumed: () -> Unit) {

    private val channel = BroadcastChannel<Int>(1)
    var retry = 0

    init {
        scope.launch { consume() }
    }

    fun sendEvent(element: Int, loadingProblem: Boolean) {
        scope.launch {
            if (loadingProblem) {
                switchRetry()
                channel.send(retry)
            } else {
                channel.send(element)
            }
        }
    }

    private fun switchRetry() {
        retry = if (retry == 0) 1 else 0
    }

    private suspend fun consume() {
        channel.asFlow()
            .debounce(TIMEOUT_MILLISECONDS)
            .distinctUntilChanged()
            .collect { onConsumed() }
    }

    companion object {
        private const val TIMEOUT_MILLISECONDS = 500L
    }
}