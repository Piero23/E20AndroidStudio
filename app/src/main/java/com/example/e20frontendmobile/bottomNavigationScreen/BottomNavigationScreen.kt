package com.example.composeuitemplates.presentation.bottomNavigationScreen


import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.e20frontendmobile.activities.MainProfileScreen
import com.example.e20frontendmobile.activities.ShowCheckout
import com.example.e20frontendmobile.activities.ShowDiscovery
import com.example.e20frontendmobile.activities.ShowEvent
import com.example.e20frontendmobile.bottomNavigationScreen.StandardBottomNavigation
import com.example.e20frontendmobile.bottomNavigationScreen.bottomNavItems
import com.example.e20frontendmobile.mainFun
import com.example.e20frontendmobile.qrScanner.QRCodeScannerWithBottomSheet


@ExperimentalMaterial3Api
@Composable
fun BottomNavigationScreen() {
    Surface(color = Color.LightGray) {
        val selectedIndex = remember { mutableIntStateOf(0) }
        var isAdmin: Boolean = true

        val navControllers = listOf(
            rememberNavController(), // per tab 0
            rememberNavController(), // per tab 1
            rememberNavController(), // per tab 2
            rememberNavController(), // per tab 3
            rememberNavController()  // per tab 4
        )

        Scaffold(
            bottomBar = {
                StandardBottomNavigation(
                    items = bottomNavItems(isAdmin),
                    index = selectedIndex,
                    navControllers = navControllers
                )
            }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                when(selectedIndex.intValue) {
                    0 -> {
                        @OptIn(ExperimentalAnimationApi::class)
                        NavHost(
                            navController = navControllers[0],
                            startDestination = "home",
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                            ) {
                            composable("home") {
                                mainFun(navControllers[0])
                            }
                            composable(
                                route = "discovery/{query}",
                                arguments = listOf(navArgument("query") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val query = backStackEntry.arguments?.getString("query") ?: ""
                                ShowDiscovery(navControllers[0], query)
                            }
                            composable("card"/*, arguments =
                                listOf(navArgument("id") { type = NavType.StringType})*/) {
//                                it.arguments?.getString("id")?.let {
//                                        id ->
//                                    contactViewModel.getContact(id).collectAsState(initial = null).value?.let {contact ->
//                                        SingleContact(contact = contact)
//                                    }
//                                }
                                ShowEvent(navControllers[0], isAdmin)
                            }
                            composable("checkout") {
                                ShowCheckout(navControllers[0])
                            }
                            composable("scanner"){
                                QRCodeScannerWithBottomSheet()
                            }
                        }
                    }
                    1 -> {
                        @OptIn(ExperimentalAnimationApi::class)
                        NavHost(
                            navController = navControllers[1],
                            startDestination = "discovery",
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        ) {
                            composable("discovery") {
                                ShowDiscovery(navControllers[1])
                            }
                            composable("card"/*, arguments =
                                listOf(navArgument("id") { type = NavType.StringType})*/) {
//                                it.arguments?.getString("id")?.let {
//                                        id ->
//                                    contactViewModel.getContact(id).collectAsState(initial = null).value?.let {contact ->
//                                        SingleContact(contact = contact)
//                                    }
//                                }
                                ShowEvent(navControllers[1], isAdmin)
                            }
                            composable("checkout") {
                                ShowCheckout(navControllers[1])
                            }
                            composable("scanner"){
                                QRCodeScannerWithBottomSheet()
                            }
                        }
                    }
                    2 -> {} //admin
                    3 -> {} //ticket
                    4 -> MainProfileScreen("mario") //navControllers[4]
                }
            }
        }
    }
}


@Composable
fun rememberAppNavController(): NavController {
    return rememberNavController()
}