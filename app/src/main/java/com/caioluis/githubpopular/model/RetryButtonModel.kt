package com.caioluis.githubpopular.model

import com.caioluis.githubpopular.Constants.RETRY_BUTTON_VIEW_TYPE

data class RetryButtonModel(
    override val viewType: Int = RETRY_BUTTON_VIEW_TYPE,
    val exception: Throwable? = null,
) : UiModel
