package com.caioluis.githubpopular.core.common.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun `truncate should return truncated string with ellipsis when length exceeds limit`() {
        val input = "This is a long string"
        val limit = 10
        val expected = "This is a ..."

        val result = input.truncate(limit)

        assertEquals(expected, result)
    }

    @Test
    fun `truncate should return original string when length is equal to limit`() {
        val input = "12345"
        val limit = 5
        val expected = "12345..."

        val result = input.truncate(limit)

        assertEquals(expected, result)
    }

    @Test
    fun `truncate should return original string when length is less than limit`() {
        val input = "Short"
        val limit = 10
        val expected = "Short"

        val result = input.truncate(limit)

        assertEquals(expected, result)
    }
}
