package com.caioluis.githubpopular.navigation

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class PullRequestsDestinationSerializationTest {
    private val json = Json

    @Test
    fun `serializes and deserializes destination with reserved URL chars`() {
        val destination = PullRequestsDestination(
            pullUrl = "https://api.test.com/repos/user/repo/pulls?state=open&sort=updated#section%2F1",
            repositoryId = 42,
            repositoryName = "repo name + kotlin/android",
        )

        val encoded = json.encodeToString(destination)
        val decoded = json.decodeFromString<PullRequestsDestination>(encoded)

        assertEquals(destination, decoded)
    }
}
