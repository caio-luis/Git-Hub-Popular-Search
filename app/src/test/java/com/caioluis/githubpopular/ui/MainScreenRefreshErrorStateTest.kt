package com.caioluis.githubpopular.ui

import androidx.paging.LoadState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MainScreenRefreshErrorStateTest {

    @Test
    fun `resolveStickyRefreshError should keep existing error while loading with empty list`() {
        val persistedError = IllegalStateException("no internet")

        val result = resolveStickyRefreshError(
            currentError = persistedError,
            refreshState = LoadState.Loading,
            itemCount = 0,
        )

        assertEquals(persistedError, result)
    }

    @Test
    fun `resolveStickyRefreshError should update with latest refresh error`() {
        val previousError = IllegalStateException("old")
        val newError = IllegalArgumentException("new")

        val result = resolveStickyRefreshError(
            currentError = previousError,
            refreshState = LoadState.Error(newError),
            itemCount = 0,
        )

        assertEquals(newError, result)
    }

    @Test
    fun `resolveStickyRefreshError should clear on successful refresh`() {
        val persistedError = IllegalStateException("old")

        val result = resolveStickyRefreshError(
            currentError = persistedError,
            refreshState = LoadState.NotLoading(endOfPaginationReached = false),
            itemCount = 0,
        )

        assertNull(result)
    }

    @Test
    fun `resolveStickyRefreshError should clear when list has items`() {
        val persistedError = IllegalStateException("old")

        val result = resolveStickyRefreshError(
            currentError = persistedError,
            refreshState = LoadState.Loading,
            itemCount = 1,
        )

        assertNull(result)
    }
}
