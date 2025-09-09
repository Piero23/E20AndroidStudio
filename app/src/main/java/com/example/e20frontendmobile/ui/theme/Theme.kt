package com.example.e20frontendmobile.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


// Color Schemes
private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    secondary = secondaryLight,
    tertiary = ternaryLight,
    error = errorLight,
    background = backgroundPlainLight,

    scrim = black,
    surface = backgroundPlainLight,
    surfaceDim = backgroundPlainLightestLight,
    surfaceBright = backgroundPlainLightLight,

    onPrimary = onPrimaryLight,
    onSecondary = onSecondaryLight,
    onTertiary = onTernaryLight,
    onError = onErrorLight,
    onBackground = onBackgroundLight,
    onSurface = onBackgroundLight
)

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    secondary = secondaryDark,
    tertiary = ternaryDark,
    error = errorDark,
    background = backgroundPlainDark,

    scrim = black,
    surface = backgroundPlainDark,
    surfaceDim = backgroundPlainLightestDark,
    surfaceBright = backgroundPlainLightDark,

    onPrimary = onPrimaryDark,
    onSecondary = onSecondaryDark,
    onTertiary = onTernaryDark,
    onError = onErrorDark,
    onBackground = onBackgroundDark,
    onSurface = onBackgroundDark
)


// Shapes
private val Shapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

// Globals

// Space Variables
val spaceExtraSmall: Dp = 6.dp
val spaceSmall: Dp= 10.dp
val spaceMedium: Dp = 16.dp
val spaceLarge: Dp = 28.dp
val spaceExtraLarge: Dp = 36.dp

// Size Variables
val sizeExtraSmall: Dp = 8.dp
val sizeSmall: Dp= 12.dp
val sizeMedium: Dp = 16.dp
val sizeLarge: Dp = 28.dp
val sizeExtraLarge: Dp = 36.dp

val iconSizeSmall: Dp = 20.dp


@Composable
fun E20FrontendMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}