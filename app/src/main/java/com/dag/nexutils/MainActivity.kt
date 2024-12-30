package com.dag.nexutils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.lifecycleScope
import com.dag.nexutils.base.AlertDialogManager
import com.dag.nexutils.base.components.CustomAlertDialog
import com.dag.nexutils.base.components.Visibility
import com.dag.nexutils.base.navigation.BottomNavbar
import com.dag.nexutils.base.navigation.DefaultNavigationHost
import com.dag.nexutils.base.navigation.DefaultNavigator
import com.dag.nexutils.base.navigation.Destination
import com.dag.nexutils.base.navigation.NavItem
import com.dag.nexutils.data.AlertDialogModel
import com.dag.nexutils.domain.DataPreferencesStore
import com.dag.nexutils.ui.theme.NexUtilsTheme
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val currentRoute = mutableStateOf<String?>(null)
    private val mainVM: MainVM by viewModels()

    @Inject
    lateinit var alertDialogManager: AlertDialogManager

    @Inject
    lateinit var defaultNavigator: DefaultNavigator

    @Inject
    lateinit var preferencesStore: DataPreferencesStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val showAlert = mutableStateOf(false)
        val alertDialogModel = mutableStateOf<AlertDialogModel?>(null)
        if (lifecycleScope.isActive) {
            lifecycleScope.launch {
                alertDialogManager.alertFlow.collect { model ->
                    alertDialogModel.value = model
                    showAlert.value = true
                }
            }
        }
        val activityResultSender = ActivityResultSender(this)
        setContent {
            NexUtilsTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            DefaultNavigationHost(
                                navigator = defaultNavigator,
                                modifier = Modifier.weight(1f),
                                activityResultSender = activityResultSender
                            ) {
                                currentRoute.value = it.destination.route
                                    ?.split(".")?.last()
                            }
                            if (mainVM.isBottomNavActive(currentRoute.value)) {
                                BottomNavbar(
                                    items = NavItem.values().asList(),
                                    currentDestination = currentRoute.value
                                        ?: Destination.HomeScreen.toString(),
                                    onClick = {
                                        mainVM.navigate(it)
                                    }
                                )
                            }
                        }
                    }
                    Visibility(case = showAlert.value && alertDialogModel.value != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f))
                                .blur(16.dp)
                                .zIndex(10f)
                        ) {
                            CustomAlertDialog(
                                alertDialogModel = alertDialogModel.value!!,
                                showAlert = showAlert,
                                defaultNavigator = defaultNavigator
                            )
                        }
                    }
                }
            }
        }
    }
}