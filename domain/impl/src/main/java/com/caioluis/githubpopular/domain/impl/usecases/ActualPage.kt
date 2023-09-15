package com.caioluis.githubpopular.domain.impl.usecases

object ActualPage {
    var pageNumber = 1
        private set

    fun reset() {
        pageNumber = 1
    }

    fun increase() {
        pageNumber++
    }
}
