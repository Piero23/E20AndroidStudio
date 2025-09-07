package com.example.e20frontendmobile.ui.theme

import com.example.e20frontendmobile.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Importing Fonts
val MuseoModerno = FontFamily(
    Font(R.font.museomoderno_light, FontWeight.Light),
    Font(R.font.museomoderno_regular, FontWeight.Normal),
    Font(R.font.museomoderno_semibold, FontWeight.SemiBold),
    Font(R.font.museomoderno_bold, FontWeight.Bold)
)

val Poppins = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_regular, FontWeight.Normal),
)

val BungeeInline = FontFamily(
    Font(R.font.bungeeinline_regular, FontWeight.Normal)
)


// Set of Material typography styles to start with
val Typography = Typography(

    // Very Big Titles (Landing Page, Headers)
    displayLarge = TextStyle(
        fontFamily = BungeeInline,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp
    ),
    displayMedium = TextStyle(
        fontFamily = BungeeInline,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = BungeeInline,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),

    // Headline (Screen Titles)
    headlineLarge = TextStyle(
        fontFamily = MuseoModerno,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = MuseoModerno,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = MuseoModerno,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),

    // Title (SubSections, AppBar, Cards)
    titleLarge = TextStyle(
        fontFamily = MuseoModerno,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 36.sp
    ),
    titleMedium = TextStyle(
        fontFamily = MuseoModerno,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 28.sp
    ),
    titleSmall = TextStyle(
        fontFamily = MuseoModerno,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 24.sp
    ),

    // Body (Main Text)
    bodyLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),

    // Label (Buttons, Forms, Badges)
    labelLarge = TextStyle(
        fontFamily = MuseoModerno,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),
    labelMedium = TextStyle(
        fontFamily = MuseoModerno,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Light,
        fontSize = 20.sp,
        lineHeight = 24.sp
    )
)