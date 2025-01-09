package com.dag.nexutils.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dag.nexutils.base.extensions.ObserveAsEvents
import com.dag.nexutils.features.document_scanner.presentation.DocumentScannerView
import com.dag.nexutils.features.home.presentation.HomeView
import com.dag.nexutils.features.splash.Splash
import com.dag.nexutils.features.text_extraction.presentation.TextExtractionView
import com.dag.nexutils.features.wallet.presentation.WalletView
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender


@Composable
fun DefaultNavigationHost(
    startDestination: Destination = Destination.Splash,
    navigator: DefaultNavigator,
    modifier: Modifier = Modifier,
    activityResultSender: ActivityResultSender,
    navBackStackEntryState: (NavBackStackEntry) -> Unit
) {
    val navController = rememberNavController()
    ObserveAsEvents(flow = navigator.navigationActions) { action ->
        when (action) {
            is NavigationAction.Navigate -> navController.navigate(
                action.destination
            ) {
                action.navOptions(this)
            }
            NavigationAction.NavigateUp -> navController.navigateUp()
        }
    }
    ObserveAsEvents(flow = navController.currentBackStackEntryFlow){
        navBackStackEntryState(it)
    }
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination
    ) {
        composable<Destination.Splash> {
            Splash()
        }
        composable<Destination.HomeScreen> {
            HomeView(navController)
        }
        composable<Destination.ScannerScreen> {
            DocumentScannerView()
        }
        composable<Destination.TextExtractionScreen> {
            TextExtractionView()
        }
        composable<Destination.WalletScreen> {
            WalletView(
                activityResultSender = activityResultSender
            )
        }
    }
}