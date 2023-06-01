package com.caioluis.githubpopular.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caioluis.githubpopular.domain.bridge.base.Response
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

abstract class BaseViewModel<T>(
    private val receiveChannel: ReceiveChannel<Response<T>>,
) : ViewModel() {

    init {
        viewModelScope.launch {
            receiveChannel.consumeEach { handle(it) }
        }
    }

    abstract fun handle(response: Response<T>)
}
