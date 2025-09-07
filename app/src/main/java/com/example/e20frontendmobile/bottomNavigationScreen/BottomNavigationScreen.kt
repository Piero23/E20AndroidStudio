package com.example.composeuitemplates.presentation.bottomNavigationScreen


import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.e20frontendmobile.CustomizableSearchBar
import com.example.e20frontendmobile.ShowDiscovery
import com.example.e20frontendmobile.ShowEvent
import com.example.e20frontendmobile.bottomNavigationScreen.StandardBottomNavigation
import com.example.e20frontendmobile.bottomNavigationScreen.bottomNavItems
import com.example.e20frontendmobile.mainFun
import kotlinx.coroutines.MainScope
import kotlin.text.contains


@ExperimentalMaterial3Api
@Composable
fun BottomNavigationScreen() {
    Surface(color = Color.LightGray) {
        val selectedIndex = remember { mutableIntStateOf(0) }
        var isAdmin: Boolean = false

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
                            composable("card"/*, arguments =
                                listOf(navArgument("id") { type = NavType.StringType})*/) {
//                                it.arguments?.getString("id")?.let {
//                                        id ->
//                                    contactViewModel.getContact(id).collectAsState(initial = null).value?.let {contact ->
//                                        SingleContact(contact = contact)
//                                    }
//                                }
                                ShowEvent()
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
                                ShowEvent()
                            }
                        }
                    }
                    2 -> {} //admin
                    3 -> {} //ticket
                    4 -> {} //profilo
                }
            }
        }
    }
}


@Composable
fun rememberAppNavController(): NavController {
    return rememberNavController()
}