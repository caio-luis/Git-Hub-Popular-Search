package com.caioluis.githubpopular.core.common.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
class ContextExtensionsTest {

    @Test
    fun `show long toast should display message`() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        context.showLongToast("hello")

        assertEquals("hello", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun `open browser intent should start view intent with url`() {
        val activity = Robolectric.buildActivity(Activity::class.java).setup().get()

        activity.openBrowserIntent("https://github.com")

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        assertNotNull(startedIntent)
        assertEquals(Intent.ACTION_VIEW, startedIntent.action)
        assertEquals("https://github.com", startedIntent.dataString)
    }

    @Test
    fun `open browser intent should show toast when start activity fails`() {
        val context = object : ContextWrapper(ApplicationProvider.getApplicationContext()) {
            override fun startActivity(intent: Intent?): Unit = throw IllegalStateException("cannot open")
        }

        context.openBrowserIntent("https://github.com")

        assertEquals("cannot open", ShadowToast.getTextOfLatestToast())
    }
}
