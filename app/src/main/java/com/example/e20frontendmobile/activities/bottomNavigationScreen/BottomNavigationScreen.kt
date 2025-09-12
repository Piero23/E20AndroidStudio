package com.example.composeuitemplates.presentation.bottomNavigationScreen


import android.content.Intent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.e20frontendmobile.activities.evento.ShowCheckout
import com.example.e20frontendmobile.activities.ShowDiscovery
import com.example.e20frontendmobile.activities.evento.ShowEvent
import com.example.e20frontendmobile.activities.evento.createEvent
import com.example.e20frontendmobile.activities.bottomNavigationScreen.StandardBottomNavigation
import com.example.e20frontendmobile.activities.bottomNavigationScreen.bottomNavItems
import com.example.e20frontendmobile.mainFun
import com.example.e20frontendmobile.activities.qrScanner.QRCodeScannerWithBottomSheet
import com.example.e20frontendmobile.activities.user.Orders
import com.example.e20frontendmobile.data.auth.AuthActivity
import com.example.e20frontendmobile.data.auth.AuthStateStorage
import com.example.e20frontendmobile.viewModels.EventViewModel
import com.example.e20frontendmobile.viewModels.UserViewModel


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

        val eventViewModel: EventViewModel = viewModel()
        val utenteViewModel: UserViewModel = viewModel()

        Scaffold(
            bottomBar = {
                StandardBottomNavigation(
                    items = bottomNavItems(isAdmin),
                    index = selectedIndex,
                    navControllers = navControllers
                )
            }
            //TODO evitare che scoppia se clicchi 2 volte la stessa tab
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
                                mainFun(navControllers[0] , eventViewModel )
                            }
                            composable(
                                route = "discovery/{query}",
                                arguments = listOf(navArgument("query") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val query = backStackEntry.arguments?.getString("query") ?: ""
                                ShowDiscovery(navControllers[0], query, eventViewModel, utenteViewModel)
                            }
                            composable("card"/*, arguments =
                                listOf(navArgument("id") { type = NavType.StringType})*/) {
//                                it.arguments?.getString("id")?.let {
//                                        id ->
//                                    contactViewModel.getContact(id).collectAsState(initial = null).value?.let {contact ->
//                                        SingleContact(contact = contact)
//                                    }
//                                }
                                ShowEvent(navControllers[0], isAdmin, eventViewModel)
                            }
                            composable("checkout") {
                                ShowCheckout(navControllers[0], eventViewModel)
                            }
                            composable("scanner"){
                                QRCodeScannerWithBottomSheet()
                            }
                            composable ("edit") {
                                createEvent(eventViewModel, true)
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
                                ShowDiscovery(navControllers[1], eventViewModel = eventViewModel, userViewModel = utenteViewModel)
                            }
                            composable("card"/*, arguments =
                                listOf(navArgument("id") { type = NavType.StringType})*/) {
//                                it.arguments?.getString("id")?.let {
//                                        id ->
//                                    contactViewModel.getContact(id).collectAsState(initial = null).value?.let {contact ->
//                                        SingleContact(contact = contact)
//                                    }
//                                }
                                ShowEvent(navControllers[1], isAdmin, eventViewModel)
                            }
                            composable("checkout") {
                                ShowCheckout(navControllers[1], eventViewModel)
                            }
                            composable("scanner"){
                                QRCodeScannerWithBottomSheet()
                            }
                            composable ("edit") {
                                createEvent(eventViewModel,true)
                            }
                        }
                    }
                    2 -> {
                        @OptIn(ExperimentalAnimationApi::class)
                        NavHost(
                            navController = navControllers[2],
                            startDestination = "create",
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        )
                        {
                            composable(route = "create"){
                                eventViewModel.clearSelection()
                                createEvent(eventViewModel)//admin
                            }
                        }
                    }
                    3 -> {
                        @OptIn(ExperimentalAnimationApi::class)
                        NavHost(
                            navController = navControllers[3],
                            startDestination = "orders",
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        )
                        {
                            composable(route = "orders") {
                                DebugTokenScreen()
                            }
                        }
                    } //TODO rimpiazzare con gli ordini
                    4 -> {
                        @OptIn(ExperimentalAnimationApi::class)
                        NavHost(
                            navController = navControllers[3],
                            startDestination = "profile",
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None }
                        )
                        {
                            composable(route = "profile") {
                                DebugTokenScreen()//RegisterScreen() //TODO gestire nav tra profilo, login e registra
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DebugTokenScreen() {
    val context = LocalContext.current
    val storage = remember { AuthStateStorage(context) }
    val userInfo = storage.getUserInfo()

    var userIN  by remember { mutableStateOf("Nulla") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Button(onClick = {
            val intent = Intent(context, AuthActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Vai al Login")
        }

        Button(onClick = {
            userIN = userInfo?.sub +"\n"
            userIN += userInfo?.roles
        }) {
            Text("Mostra JWT Claims")
        }

        Button(onClick = {
            //println("MAAAAAAAMMAAAAA"+getMe(context))
        }) {
            Text("Testa")
        }
        Spacer(Modifier.height(16.dp))

        Text(
            text = userIN
        )
    }
}