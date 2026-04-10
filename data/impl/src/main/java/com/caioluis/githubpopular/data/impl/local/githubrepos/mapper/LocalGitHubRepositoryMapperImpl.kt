package com.caioluis.githubpopular.data.impl.local.githubrepos.mapper

import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository
import javax.inject.Inject

class LocalGitHubRepositoryMapperImpl @Inject constructor() : LocalGitHubRepositoryMapper {
    override fun mapToDomain(localRepository: LocalGitHubRepository) = DomainGitHubRepository(
        id = localRepository.id,
        title = localRepository.title,
        description = localRepository.description,
        pullsUrl = localRepository.pullsUrl,
        stargazersCount = localRepository.stargazersCount,
        forksCount = localRepository.forksCount,
        htmlUrl = localRepository.repositoryUrl,
        page = localRepository.page,
        language = localRepository.language,
        userName = localRepository.userName,
        avatarUrl = localRepository.avatarUrl,
    )

    override fun mapToLocal(domainRepository: DomainGitHubRepository) = LocalGitHubRepository(
        id = domainRepository.id,
        title = domainRepository.title,
        description = domainRepository.description,
        pullsUrl = domainRepository.pullsUrl,
        stargazersCount = domainRepository.stargazersCount,
        forksCount = domainRepository.forksCount,
        repositoryUrl = domainRepository.htmlUrl,
        page = domainRepository.page,
        language = domainRepository.language,
        userName = domainRepository.userName,
        avatarUrl = domainRepository.avatarUrl,
    )
}
