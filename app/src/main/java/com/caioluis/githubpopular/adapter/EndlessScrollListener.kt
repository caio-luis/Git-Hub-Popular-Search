package com.caioluis.githubpopular.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caioluis.domain.base.throttleFirst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

const val VISIBLE_THRESHOLD = 5
const val DEBOUNCE_TIMEOUT_MILLIS = 2500L

abstract class EndlessScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val coroutineScope: CoroutineScope,
) : RecyclerView.OnScrollListener() {

    private val output = Channel<Unit>()

    fun start() {
        coroutineScope.launch {
            output
                .receiveAsFlow()
                .throttleFirst(DEBOUNCE_TIMEOUT_MILLIS)
                .collect { onLoadMoreItems() }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy < 0) return

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRESHOLD)
            output.trySend(Unit)
    }

    abstract fun onLoadMoreItems()

    fun dispose() = output.close()
}