package com.caioluis.githubpopular.ui

import com.caioluis.githubpopular.model.UiGitHubRepository
import com.caioluis.githubpopular.model.UiRepositoryOwner

object PreviewsData {
    fun getPreviewData(size: Int) = mutableListOf<UiGitHubRepository>().apply {
        repeat(10) {
            this.add(
                UiGitHubRepository(
                    name = "Sample",
                    stargazersCount = 1000,
                    forksCount = 10,
                    description = "This is the repository description.",
                    owner = UiRepositoryOwner(
                        login = "sample",
                    )
                )
            )
        }
    }
}