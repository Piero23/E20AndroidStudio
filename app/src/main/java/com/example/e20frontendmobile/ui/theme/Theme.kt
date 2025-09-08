package com.example.e20frontendmobile.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

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
    onBackground = textColorLight,
    onSurface = onSurfaceLight
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
    onSurface = onSurfaceDark,
    onBackground = textColorDark
)


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
        content = content
    )
}