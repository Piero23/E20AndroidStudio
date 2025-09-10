package com.example.e20frontendmobile.activities.bottomNavigationScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun StandardBottomNavigation(
    index: MutableState<Int>,
    items: List<BottomNavItem>,
    navControllers: List<NavHostController>
) {

    Row(
        modifier = Modifier.fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = index.value == item.index,
                onClick = {
                    if (index.value == item.index) {
                        val navController = navControllers[item.index]
                        navController.popBackStack(
                            route = navController.graph.startDestinationRoute!!,
                            inclusive = false
                        )
                    } else {
                        index.value = item.index
                    }
                },
                icon = { item.icon?.let {
                    Icon(it, contentDescription = item.contentDescription )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF2196F3),
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }

    }
}

