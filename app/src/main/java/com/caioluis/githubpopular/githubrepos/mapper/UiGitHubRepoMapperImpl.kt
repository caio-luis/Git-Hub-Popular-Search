package com.caioluis.githubpopular.githubrepos.mapper

import com.caioluis.githubpopular.core.common.extensions.truncate
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import com.caioluis.githubpopular.githubrepos.model.UiGitHubRepo
import javax.inject.Inject

class UiGitHubRepoMapperImpl @Inject constructor() : UiGitHubRepoMapper {

    override fun mapToUi(domainRepo: DomainGitHubRepository): UiGitHubRepo = UiGitHubRepo(
        id = domainRepo.id,
        title = domainRepo.title,
        description = domainRepo.description.truncate(DESCRIPTION_CHAR_LIMIT),
        pullsUrl = domainRepo.pullsUrl.replace(PULLS_URL_SUFFIX, ""),
        stargazersCount = domainRepo.stargazersCount,
        repositoryUrl = domainRepo.htmlUrl,
        forksCount = domainRepo.forksCount,
        userName = domainRepo.userName,
        avatarUrl = domainRepo.avatarUrl,
    )

    companion object {
        private const val DESCRIPTION_CHAR_LIMIT = 200
        private const val PULLS_URL_SUFFIX = "{/number}"
    }
}
