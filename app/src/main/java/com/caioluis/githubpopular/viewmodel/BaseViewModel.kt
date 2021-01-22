package com.caioluis.githubpopular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caioluis.domain.base.Response
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

/**
 * Created by Caio Luis (caio-luis) on 11/10/20
 */

abstract class BaseViewModel<T>(
    private val receiveChannel: ReceiveChannel<Response<T>>
) : ViewModel() {

    init {
        viewModelScope.launch { receiveChannel.consumeEach { handle(it) } }
    }

    abstract fun handle(response: Response<T>)

    override fun onCleared() {
        receiveChannel.cancel()
        viewModelScope.cancel()
        super.onCleared()
    }
}