package com.caioluis.githubpopular.core.common.utils

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class LogUtilTest {

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.w(any(), any<String>(), any()) } returns 0

        LogUtil.isDebug = true
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `d calls Log d when isDebug is true`() {
        val tag = "Tag"
        val message = "Message"

        LogUtil.d(tag, message)

        verify(exactly = 1) { Log.d(tag, message) }
    }

    @Test
    fun `e calls Log e when isDebug is true`() {
        val tag = "Tag"
        val message = "Message"
        val throwable = Throwable("Error")

        LogUtil.e(tag, message, throwable)

        verify(exactly = 1) { Log.e(tag, message, throwable) }
    }

    @Test
    fun `i calls Log i when isDebug is true`() {
        val tag = "Tag"
        val message = "Message"

        LogUtil.i(tag, message)

        verify(exactly = 1) { Log.i(tag, message) }
    }

    @Test
    fun `w calls Log w when isDebug is true`() {
        val tag = "Tag"
        val message = "Message"
        val throwable = Throwable("Error")

        LogUtil.w(tag, message, throwable)

        verify(exactly = 1) { Log.w(tag, message, throwable) }
    }

    @Test
    fun `d does not call Log d when isDebug is false`() {
        LogUtil.isDebug = false
        val tag = "Tag"
        val message = "Message"

        LogUtil.d(tag, message)

        verify(exactly = 0) { Log.d(tag, message) }
    }
}
