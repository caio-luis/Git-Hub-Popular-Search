package com.caioluis.githubpopular.data.impl.local.githubrepos.mapper

import com.caioluis.githubpopular.data.bridge.local.githubrepos.entity.LocalGitHubRepository
import com.caioluis.githubpopular.domain.bridge.entity.DomainGitHubRepository

interface LocalGitHubRepositoryMapper {
    fun mapToDomain(localRepository: LocalGitHubRepository): DomainGitHubRepository

    fun mapToLocal(domainRepository: DomainGitHubRepository): LocalGitHubRepository
}
