package com.example.e20frontendmobile.bottomNavigationScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp

fun bottomNavItems(isAdmin: Boolean): List<BottomNavItem> {
    return if (isAdmin) {
        listOf(
            BottomNavItem(
                route = "home",
                contentDescription = "Home",
                icon = Icons.Default.Home
            ),
            BottomNavItem(
                route = "search",
                contentDescription = "Search",
                icon = Icons.Default.Search
            ),
            BottomNavItem(
                route = "add-event",
                contentDescription = "Azione Admin",
                icon = Icons.Default.Add
            ),
            BottomNavItem(
                route = "orders",
                contentDescription = "Notification",
                icon = Icons.Default.ThumbUp
            ),
            BottomNavItem(
                route = "profile",
                contentDescription = "Profile",
                icon = Icons.Default.Person
            )
        )
    } else {
        listOf(
            BottomNavItem(
                route = "home",
                contentDescription = "Home",
                icon = Icons.Default.Home
            ),
            BottomNavItem(
                route = "search",
                contentDescription = "Search",
                icon = Icons.Default.Search
            ),
            BottomNavItem(
                route = "orders",
                contentDescription = "Notification",
                icon = Icons.Default.ThumbUp
            ),
            BottomNavItem(
                route = "profile",
                contentDescription = "Profile",
                icon = Icons.Default.Person
            )
        )
    }
}