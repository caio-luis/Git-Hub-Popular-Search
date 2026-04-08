package com.caioluis.githubpopular.githubrepos.mapper

import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.githubrepos.model.UiGitHubRepo

interface UiGitHubRepoMapper {
    fun mapToUi(domainRepo: DomainGitHubRepository): UiGitHubRepo
}
