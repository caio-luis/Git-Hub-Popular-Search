package com.caioluis.githubpopular.adapter

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val VISIBLE_THRESHOLD = 5

abstract class EndlessScrollListener(
    private val layoutManager: LinearLayoutManager,
) : RecyclerView.OnScrollListener() {

    private var isLoading = true
    private var lastTotalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy < 0) return

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (isLoading && totalItemCount > lastTotalItemCount) {
            isLoading = false
            lastTotalItemCount = totalItemCount
        }

        if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRESHOLD) {
            isLoading = true
            onLoadMoreItems()
            Log.d("EndlessScrollListener", "loading: $isLoading")
        }
    }

    fun reset() {
        lastTotalItemCount = 0
        isLoading = true
    }

    abstract fun onLoadMoreItems()
}