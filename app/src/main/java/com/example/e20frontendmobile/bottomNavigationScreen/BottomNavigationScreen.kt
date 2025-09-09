package com.example.composeuitemplates.presentation.bottomNavigationScreen


import android.content.Intent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.e20frontendmobile.activities.MainProfileScreen
import com.example.e20frontendmobile.activities.ShowDiscovery
import com.example.e20frontendmobile.activities.ShowEvent
import com.example.e20frontendmobile.bottomNavigationScreen.StandardBottomNavigation
import com.example.e20frontendmobile.bottomNavigationScreen.bottomNavItems
import com.example.e20frontendmobile.mainFun
import com.example.e20frontendmobile.auth.AuthActivity


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
                    3 -> {
                        val context = LocalContext.current

                        Button(onClick = {
                            val intent = Intent(context, AuthActivity::class.java)
                            context.startActivity(intent)
                        }) {
                            Text("Vai al Login")
                        }
                    } //ticket
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