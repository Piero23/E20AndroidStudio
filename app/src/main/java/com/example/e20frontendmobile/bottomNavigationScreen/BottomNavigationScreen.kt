package com.example.composeuitemplates.presentation.bottomNavigationScreen


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e20frontendmobile.ShowEvent
import com.example.e20frontendmobile.bottomNavigationScreen.Sample.SampleScreen1
import com.example.e20frontendmobile.bottomNavigationScreen.Sample.SampleScreen2
import com.example.e20frontendmobile.bottomNavigationScreen.Sample.SampleScreen3
import com.example.e20frontendmobile.bottomNavigationScreen.Sample.SampleScreen4
import com.example.e20frontendmobile.bottomNavigationScreen.StandardScaffold
import com.example.e20frontendmobile.bottomNavigationScreen.bottomNavItems

@ExperimentalMaterial3Api
@Composable
fun BottomNavigationScreen() {
    Surface(color = Color.LightGray) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        var isAdmin: Boolean = false
        StandardScaffold(
            bottomNavItems = bottomNavItems(isAdmin),
            navController = navController,
//            showBottomBar = shouldShowBottomBar(navBackStackEntry)
        ) {
//            Navigation(navController = navController)
            NavHost(navController = navController, startDestination = "sample_screen-1") {
                composable(route = "sample_screen-1") {
                    ShowEvent()
                }
                composable(route = "sample_screen-2") {
                    SampleScreen2()
                }
                composable(route = "sample_screen-3") {
                    SampleScreen3()
                }
                composable(route = "sample_screen-4") {
                    SampleScreen4()
                }
                if (isAdmin) {
                    composable("admin_action") {
                        // schermata o azione speciale admin
                    }
                }
            }
        }
    }
}