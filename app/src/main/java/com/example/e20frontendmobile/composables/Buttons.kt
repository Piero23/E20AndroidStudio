package com.example.e20frontendmobile.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme
import com.example.e20frontendmobile.ui.theme.buttonGradientType1
import com.example.e20frontendmobile.ui.theme.iconSizeSmall
import com.example.e20frontendmobile.ui.theme.sizeLarge
import com.example.e20frontendmobile.ui.theme.sizeMedium
import com.example.e20frontendmobile.ui.theme.sizeSmall
import com.example.e20frontendmobile.ui.theme.spaceSmall


@Composable
fun IconTextButtonType1(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    textSize: TextUnit = 24.sp,
    cornerSize: Dp = sizeMedium,
    withIcon: Boolean = false,
    icon: ImageVector = Icons.Filled.Star,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier
            .background(
                brush = buttonGradientType1(),
                shape = RoundedCornerShape(cornerSize)
            )
            .padding(sizeSmall, 0.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text,
                style = MaterialTheme.typography.labelLarge.copy(fontSize = textSize),
                color = MaterialTheme.colorScheme.onPrimary
            )
            if (withIcon) {
                Spacer(Modifier.width(sizeSmall))
                
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.size(sizeLarge),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}


@Composable
fun IconButtonType1(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Star,
    iconDescription: String,
    iconSize: Dp = iconSizeSmall,
    cornerSize: Dp = iconSizeSmall.div(2),
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(spaceSmall),
        modifier = modifier
            .size(iconSize.times(2f))
            .background(
                brush = buttonGradientType1(),
                shape = RoundedCornerShape(cornerSize)
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(iconSize),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}


// Previews ----------------------------------------------------------------------------------------


//@Preview
@Composable
fun ButtonPreview() {
    E20FrontendMobileTheme(darkTheme = false) {
        IconTextButtonType1(
            text = "Log Out",
            onClick = { }
        )
    }
}

@Preview
@Composable
fun ButtonPreview2() {
    E20FrontendMobileTheme(darkTheme = false) {
        IconButtonType1(
            onClick = { },
            icon = Icons.Default.Edit,
            iconDescription = "",
            iconSize = 40.dp,
            //modifier = Modifier.size(40.dp),
        )
    }
}

@Preview
@Composable
fun ButtonPreview40() {
    E20FrontendMobileTheme(darkTheme = false) {
        IconButtonType1(
            onClick = { },
            icon = Icons.Default.CheckCircle,
            iconDescription = "",
            iconSize = 40.dp,
            //modifier = Modifier.size(40.dp),
        )
    }
}

@Preview
@Composable
fun ButtonPreview30() {
    E20FrontendMobileTheme(darkTheme = false) {
        IconButtonType1(
            onClick = { },
            icon = Icons.Default.CheckCircle,
            iconDescription = "",
            iconSize = 30.dp,
            //modifier = Modifier.size(40.dp),
        )
    }
}

@Preview
@Composable
fun ButtonPreview20() {
    E20FrontendMobileTheme(darkTheme = false) {
        IconButtonType1(
            onClick = { },
            icon = Icons.Default.CheckCircle,
            iconDescription = "",
            iconSize = 20.dp,
            //modifier = Modifier.size(40.dp),
        )
    }
}

//@Preview
@Composable
fun ButtonPreview3() {
    E20FrontendMobileTheme(darkTheme = false) {
        IconTextButtonType1(
            text = "Log Out",
            onClick = { },
            withIcon = true,
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            modifier = Modifier.width(300.dp).height(100.dp),
            cornerSize = 30.dp
        )
    }
}


