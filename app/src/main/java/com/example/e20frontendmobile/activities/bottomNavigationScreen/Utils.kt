package com.example.e20frontendmobile.activities.bottomNavigationScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable

@Composable
fun bottomNavItems(isAdmin: Boolean): List<BottomNavItem> {
    return if (isAdmin) {
        listOf(
            BottomNavItem(
                index = 0,
                contentDescription = "Home",
                icon = Icons.Default.Home
            ),
            BottomNavItem(
                index = 1,
                contentDescription = "Search",
                icon = Icons.Default.Search
            ),
            BottomNavItem(
                index = 2,
                contentDescription = "Azione Admin",
                icon = Icons.Default.Add
            ),
            BottomNavItem(
                index = 3,
                contentDescription = "Notification",
                icon = Icons.Filled.ShoppingBag //TODO da decidere
            ),
            BottomNavItem(
                index = 4,
                contentDescription = "Profile",
                icon = Icons.Default.Person
            )
        )
    } else {
        listOf(
            BottomNavItem(
                index = 0,
                contentDescription = "Home",
                icon = Icons.Default.Home
            ),
            BottomNavItem(
                index = 1,
                contentDescription = "Search",
                icon = Icons.Default.Search
            ),
            BottomNavItem(
                index = 3,
                contentDescription = "Notification",
                icon = Icons.Default.ShoppingBag
            ),
            BottomNavItem(
                index = 4,
                contentDescription = "Profile",
                icon = Icons.Default.Person
            )
        )
    }
}