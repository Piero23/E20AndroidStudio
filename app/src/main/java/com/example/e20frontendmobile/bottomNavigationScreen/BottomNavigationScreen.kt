package com.example.composeuitemplates.presentation.bottomNavigationScreen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e20frontendmobile.CustomizableSearchBar
import com.example.e20frontendmobile.ShowDiscovery
import com.example.e20frontendmobile.ShowEvent
import com.example.e20frontendmobile.bottomNavigationScreen.Sample.SampleScreen3
import com.example.e20frontendmobile.bottomNavigationScreen.Sample.SampleScreen4
import com.example.e20frontendmobile.bottomNavigationScreen.StandardScaffold
import com.example.e20frontendmobile.bottomNavigationScreen.bottomNavItems
import com.example.e20frontendmobile.mainFun
import kotlinx.coroutines.MainScope
import kotlin.text.contains


@ExperimentalMaterial3Api
@Composable
fun BottomNavigationScreen() {
    Surface(color = Color.LightGray) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        var isAdmin: Boolean = true
        StandardScaffold(
            bottomNavItems = bottomNavItems(isAdmin),
            navController = navController,
//            showBottomBar = shouldShowBottomBar(navBackStackEntry)
        ) {
//            Navigation(navController = navController)
            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") {
                    mainFun(navController)
                }
                composable(route = "search") {
                    ShowDiscovery()
                }
                composable(route = "orders") {
                    SampleScreen3()
                }
                composable(route = "profile") {
                    SampleScreen4()
                }
                composable("showEvent") {
                    ShowEvent()
                }
                composable("add-event") {

                }
            }
        }
    }
}

@Composable
fun rememberAppNavController(): NavController {
    return rememberNavController()
}