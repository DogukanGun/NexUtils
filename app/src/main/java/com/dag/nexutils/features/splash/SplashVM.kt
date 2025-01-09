package com.dag.nexutils.features.splash

import androidx.lifecycle.viewModelScope
import com.dag.nexutils.base.BaseVM
import com.dag.nexutils.base.navigation.DefaultNavigator
import com.dag.nexutils.base.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SplashVM @Inject constructor(
    private val navigator: DefaultNavigator,
): BaseVM<SplashVS>(){
    fun startApplication(){
        viewModelScope.launch {
            runBlocking {
                delay(5)
            }
            navigator.navigate(Destination.HomeScreen){
                this.popUpTo(0)
            }
        }
    }
}