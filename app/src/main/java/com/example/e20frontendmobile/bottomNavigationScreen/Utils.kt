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
                route = "sample_screen-1",
                contentDescription = "Home",
                icon = Icons.Default.Home
            ),
            BottomNavItem(
                route = "sample_screen-2",
                contentDescription = "Search",
                icon = Icons.Default.Search
            ),
            BottomNavItem(
                route = "admin_action",
                contentDescription = "Azione Admin",
                icon = Icons.Default.Add
            ),
            BottomNavItem(
                route = "sample_screen-3",
                contentDescription = "Notification",
                icon = Icons.Default.ThumbUp
            ),
            BottomNavItem(
                route = "sample_screen-4",
                contentDescription = "Profile",
                icon = Icons.Default.Person
            )
        )
    } else {
        listOf(
            BottomNavItem(
                route = "sample_screen-1",
                contentDescription = "Home",
                icon = Icons.Default.Home
            ),
            BottomNavItem(
                route = "sample_screen-2",
                contentDescription = "Search",
                icon = Icons.Default.Search
            ),
            BottomNavItem(
                route = "sample_screen-3",
                contentDescription = "Notification",
                icon = Icons.Default.ThumbUp
            ),
            BottomNavItem(
                route = "sample_screen-4",
                contentDescription = "Profile",
                icon = Icons.Default.Person
            )
        )
    }
}