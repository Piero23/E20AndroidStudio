package com.example.e20frontendmobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.collections.List


// Helper Class to use Percentages
data class Percent(val x: Float, val y: Float)

// Internal Gradients
@Composable
fun linearGradient(
    colors: List<Color>,
    start: Percent = Percent(0f, 0f),
    end: Percent = Percent(1f, 1f),
): Brush {
    var size by remember { mutableStateOf(Size.Zero) }

    return if (size != Size.Zero) {
        Brush.linearGradient(
            colors = colors,
            start = Offset(size.width * start.x, size.height * start.y),
            end = Offset(size.width * end.x, size.height * end.y)
        )
    } else {
        // fallback iniziale, verrà aggiornato dopo che size è disponibile
        Brush.linearGradient(colors)
    }
}

// Theme Custom Gradients
@Composable
fun backgroundGradient(): Brush =
    if (isSystemInDarkTheme()) Brush.verticalGradient(listOf(
            backgroundGradientStop1Dark,
            backgroundGradientStop2Dark,
            backgroundGradientStop3Dark,
            backgroundGradientStop4Dark,
            backgroundGradientStop5Dark
        ))
    else Brush.verticalGradient(listOf(
            backgroundGradientStop1Light,
            backgroundGradientStop2Light,
            backgroundGradientStop3Light,
            backgroundGradientStop4Light,
            backgroundGradientStop5Light
        ))

@Composable
fun imageOverlayGradient(
    start: Percent = Percent(0f, 0f),
    end: Percent = Percent(1f, 1f),
): Brush = linearGradient(
    colors = listOf(overlayBlack10, overlayBlack40),
    start = start,
    end = end
)


@Composable
fun buttonGradientType1(): Brush = linearGradient(
    colors = if(isSystemInDarkTheme()) listOf(buttonGradientType1FirstDark, buttonGradientType1LastDark)
             else listOf(buttonGradientType1FirstLight, buttonGradientType1LastLight),

    start = Percent(0f, 0.5f),
    end = Percent(1f, 0.5f)
)


