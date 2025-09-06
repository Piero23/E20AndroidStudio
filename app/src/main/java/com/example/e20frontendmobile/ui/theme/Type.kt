package com.example.e20frontendmobile.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.e20frontendmobile.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val museoModerno = FontFamily(
    Font(R.font.museomoderno_bold, FontWeight.Bold),
    Font(R.font.museomoderno_thin, FontWeight.Thin),
    Font(R.font.museomoderno_black, FontWeight.Black),
    Font(R.font.museomoderno_light, FontWeight.Light),
    Font(R.font.museomoderno_medium, FontWeight.Medium),
    Font(R.font.museomoderno_extrabold, FontWeight.ExtraBold),
    Font(R.font.museomoderno_extralight, FontWeight.ExtraLight),
    Font(R.font.museomoderno_regular, FontWeight.Normal),
    Font(R.font.museomoderno_semibold, FontWeight.SemiBold),
)