package com.example.e20frontendmobile.activities.bottomNavigationScreen

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val index: Int,
    val contentDescription: String? = null,
    val icon: ImageVector? = null,
//    val selectedIcon: Int? = null
)
