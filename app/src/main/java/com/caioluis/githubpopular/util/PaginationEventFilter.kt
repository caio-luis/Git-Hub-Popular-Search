package com.caioluis.githubpopular.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Created by Caio Luis (@caio.luis) on 26/12/20
 */

class PaginationEventFilter(val scope: CoroutineScope, val onConsumed: () -> Unit) {

    private val channel = BroadcastChannel<Int>(1)

    init {
        scope.launch { consume() }
    }

    fun sendEvent(element: Int) {
        scope.launch {
            channel.send(element)
        }
    }

    private suspend fun consume() {
        channel.asFlow()
            .distinctUntilChanged()
            .collect { onConsumed() }
    }
}