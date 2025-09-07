package com.example.e20frontendmobile.ui.theme

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb


// Custom Drop Shadow Modifier
fun Modifier.blurredDropShadow(
    shadowColor: Color = black.copy(0.2f),
    blurRadius: Float = 20f,
    offset: Offset = Offset.Zero,
    shadowCornerRadius: Float = spaceMedium.value
) : Modifier {
    return this.then(
        Modifier
                .drawBehind {
                    drawIntoCanvas { canvas ->

                        // Paint Android classico
                        val frameworkPaint = android.graphics.Paint().apply {
                            color = shadowColor.toArgb()
                            maskFilter = BlurMaskFilter(blurRadius.times(4f), BlurMaskFilter.Blur.NORMAL)
                        }

                        // Lo converto in Compose Paint
                        val paint = Paint().apply {
                            asFrameworkPaint().set(frameworkPaint)
                        }

                        canvas.drawRoundRect(
                            left = offset.x, top = offset.y,
                            right = size.width + offset.x, bottom = size.height + offset.y,

                            radiusX = shadowCornerRadius.times(2f), radiusY = shadowCornerRadius.times(2f),

                            paint = paint
                        )
                    }
                }
            )
}

// Internal Gradients
@Composable
fun linearGradient(
    colors: List<Color>,
    start: Offset = Offset(0f, 0f),
    end: Offset = Offset(1f, 1f),
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
    start: Offset = Offset(0f, 0f),
    end: Offset = Offset(1f, 1f),
): Brush = linearGradient(
    colors = listOf(overlayBlack10, overlayBlack40),
    start = start,
    end = end
)


@Composable
fun buttonGradientType1(): Brush = linearGradient(
    colors = if(isSystemInDarkTheme()) listOf(buttonGradientType1FirstDark, buttonGradientType1LastDark)
             else listOf(buttonGradientType1FirstLight, buttonGradientType1LastLight),

    start = Offset(0f, 0.5f),
    end = Offset(1f, 0.5f)
)
