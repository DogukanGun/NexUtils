package com.dag.nexutils.base

import com.dag.nexutils.base.navigation.Destination
import com.dag.nexutils.data.AlertDialogButton
import com.dag.nexutils.data.AlertDialogButtonType
import com.dag.nexutils.data.AlertDialogModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.runBlocking

class AlertDialogManager {
    private val _alertFlow = MutableSharedFlow<AlertDialogModel>(replay = 0)
    val alertFlow: SharedFlow<AlertDialogModel> = _alertFlow

    suspend fun showAlert(alertDialogModel: AlertDialogModel) {
        _alertFlow.emit(alertDialogModel)
    }

    fun showComingSoonMessage(){
        runBlocking(Dispatchers.IO) {
            _alertFlow.emit(
                AlertDialogModel(
                    "Coming Soon!",
                    "This feature is under development. Please be patient.",
                    positiveButton = AlertDialogButton(
                        text = "Go Back",
                        navigate = Destination.HomeScreen,
                        type = AlertDialogButtonType.NAVIGATE
                    )
                )
            )
        }
    }

    fun generateServerErrorMessage() {
        runBlocking(Dispatchers.IO) {
            _alertFlow.emit(
                AlertDialogModel(
                    "Server error !",
                    "Unexpected error has happened at our server. We\'re looking on it",
                    positiveButton = AlertDialogButton(
                        text = "Go Back",
                        navigate = Destination.HomeScreen,
                        type = AlertDialogButtonType.NAVIGATE
                    )
                )
            )
        }
    }
}