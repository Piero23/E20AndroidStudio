package com.example.e20frontendmobile.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e20frontendmobile.ui.theme.buttonGradientType1FirstLight
import com.example.e20frontendmobile.ui.theme.buttonGradientType1LastLight
import com.example.e20frontendmobile.ui.theme.linearGradient

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    value: String,
    readOnly: Boolean = false
) {
    var height by remember { mutableStateOf(10.dp) }
    var width by remember { mutableStateOf(10.dp) }
    val density = LocalDensity.current

    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.background(Color.White)
            .width(300.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        BasicTextField(
            enabled = !readOnly,
            value = value,
            onValueChange = onValueChange,
            cursorBrush = linearGradient(listOf(buttonGradientType1FirstLight, buttonGradientType1LastLight)),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = singleLine,
            decorationBox = { innerTextField ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            height = with(density) { coordinates.size.height.toDp() }
                            width = with(density) { coordinates.size.width.toDp() }
                        }
                        .border(
                            brush = linearGradient(listOf(buttonGradientType1FirstLight, buttonGradientType1LastLight)),
                            width = 2.dp,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    // Mostra il placeholder solo se il campo non è in focus E il testo è vuoto
                    if (!isFocused && value.isEmpty() && placeholder != null) {
                        androidx.compose.material3.Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                    }

                    Spacer(modifier = Modifier.padding(3.dp))
                    innerTextField()  // Mostra il testo inserito
                }
            }
        )

        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0.5f to Color.Transparent,
                            0.51f to Color.White
                        )
                    ),
                    shape = RoundedCornerShape(11.dp)
                )
                .height(height.minus(3f.dp))
                .width(30.dp)
        ) {}

        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.5f to Color.White,
                            0.51f to Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(11.dp)
                )
                .height(20.dp)
                .width(width.minus(3f.dp))
        ) {}

    }
}

@Composable
@Preview
fun previewCustomTextField() {
    var value = ""
    CustomTextField(value = value,
        onValueChange = { value = it },
        placeholder = "Nome",
        singleLine = true,
    )
}