package com.example.e20frontendmobile.bottomNavigationScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardScaffold(
    bottomNavItems: List<BottomNavItem>,
    navController: NavController,
    content: @Composable () -> Unit
) {

    Scaffold(
//        floatingActionButton = {
//            if (admin) FloatingActionButton(
//                modifier = Modifier.size(50.dp),
//                onClick = { /* azione centrale */ },
//                containerColor = Color(0xFF2196F3),
//                contentColor = Color.White
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "Azione")
//            }
//        },
//        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            StandardBottomNavigation(
                items = bottomNavItems,
                navController = navController
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            content()
        }
    }
}