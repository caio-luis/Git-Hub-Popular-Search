package com.caioluis.githubpopular.core.common.exception

interface ErrorMapper {
    fun map(throwable: Throwable): AppException
}
