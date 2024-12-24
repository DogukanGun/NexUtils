package com.dag.nexutils.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseVM<T: BaseVS> : ViewModel() {

    private val _viewState = MutableStateFlow(null)
    val viewState: StateFlow<T?> get() = _viewState

}