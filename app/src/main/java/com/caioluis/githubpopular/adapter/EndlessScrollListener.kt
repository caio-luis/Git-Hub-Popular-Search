package com.caioluis.githubpopular.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caioluis.data.base.throttleFirst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

const val VISIBLE_THRESHOLD = 5
const val DEBOUNCE_TIMEOUT_MILLIS = 2500L

abstract class EndlessScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val coroutineScope: CoroutineScope,
) :
    RecyclerView.OnScrollListener() {

    private val output = MutableSharedFlow<Unit>(
        onBufferOverflow = BufferOverflow.DROP_LATEST,
        extraBufferCapacity = 1
    )

    fun startListeningToEvents() {
        coroutineScope.launch {
            output
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
            output.tryEmit(Unit)
    }

    abstract fun onLoadMoreItems()
}