package com.example.e20frontendmobile.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e20frontendmobile.composables.IconButtonType1
import com.example.e20frontendmobile.composables.IconTextButtonType1
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme
import com.example.e20frontendmobile.ui.theme.backgroundGradient
import com.example.e20frontendmobile.ui.theme.backgroundLinearGradient
import com.example.e20frontendmobile.ui.theme.blurredDropShadow
import com.example.e20frontendmobile.ui.theme.linearGradient
import com.example.e20frontendmobile.ui.theme.spaceExtraSmall
import com.example.e20frontendmobile.ui.theme.spaceLarge
import com.example.e20frontendmobile.ui.theme.spaceMedium
import com.example.e20frontendmobile.ui.theme.white

class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            E20FrontendMobileTheme {}
        }
    }
}

// Components --------------------------------------------------------------------------------------
@Composable
fun TitledBox(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .blurredDropShadow(
                shadowColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.25f),
                offset = Offset(10f,10f),
                blurRadius = 10f,
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = MaterialTheme.shapes.medium
            )
            .padding(top = 14.dp, start = 20.dp, bottom = 4.dp, end = 20.dp)

    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = content,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 44.sp),
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Composable
fun UserImage(
    username: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(MaterialTheme.colorScheme.tertiary)
    ) {
        Text(
            text = username[0].uppercase(),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 80.sp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun UserInfo(
    firstName: String, lastName: String, email: String, birthDate: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopEnd,

        modifier = modifier
            .blurredDropShadow(
                shadowColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.25f),
                offset = Offset(10f,10f),
                blurRadius = 10f,
            )
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = MaterialTheme.shapes.medium
            )
            .padding(spaceMedium)


    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = firstName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Text(
                text = lastName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Spacer(Modifier.height(spaceMedium))
            Text(
                text = email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Text(
                text = birthDate,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
        IconButtonType1(
            onClick = { },
            icon = Icons.Default.Edit,
            iconDescription = "",
            iconSize = 20.dp,
            //modifier = Modifier.size(60.dp),
        )
    }
}


@Composable
fun LogOutBox() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Text(
            text = "Cambia Password",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .8f),

            overflow = TextOverflow.Visible,
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp, 0.dp, 0.dp, 0.dp)
        )

        Spacer(Modifier.width(spaceLarge))

        IconTextButtonType1(
            onClick = {},
            text = "Log Out",
            withIcon = true,
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            modifier = Modifier
                .blurredDropShadow(
                    shadowColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.25f),
                    offset = Offset(10f,10f),
                    blurRadius = 10f,
                )
        )
    }
}


@Composable
fun MainScreen(
    username: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient())
            .padding(top = 114.dp, start = 40.dp, bottom = 61.dp, end = 40.dp)
    ) {
        // User Circle with Initial
        UserImage(username = username)
        Spacer(Modifier.height(spaceExtraSmall))

        // Username
        Text(
            text = "@$username",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(Modifier.height(spaceLarge))

        // Seguaci & Seguiti Box View
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitledBox("Seguaci", "123", Modifier.weight(1f))
            Spacer(Modifier.width(spaceMedium))
            TitledBox("Seguiti", "456", Modifier.weight(1f))
        }

        Spacer(Modifier.height(spaceMedium))

        // User Info
        UserInfo("Mario", "Rossi", "mario.rossi@gmail.com", "2000-01-01")

        Spacer(Modifier.height(spaceMedium))

        // Log Out Button & Change Password
        LogOutBox()
    }
}


@Composable
fun LogInScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .backgroundLinearGradient(
                colors = listOf(
                        MaterialTheme.colorScheme.tertiary,
                        MaterialTheme.colorScheme.secondary)
            )
            .padding(top = 114.dp, start = 40.dp, bottom = 61.dp, end = 40.dp)
    ) {}
}

// Previews ----------------------------------------------------------------------------------------

//@Preview
@Composable
fun MainScreenPreview() {
    E20FrontendMobileTheme(darkTheme = false) {
        MainScreen(
            username = "username"
        )
    }
}

@Preview
@Composable
fun LogInScreenPreview() {
    E20FrontendMobileTheme(darkTheme = false) {
        LogInScreen()
    }
}

//@Preview
@Composable
fun UserImagePreview() {
    E20FrontendMobileTheme {
        UserImage(username = "username")
    }
}

//@Preview
@Composable
fun SeguaciPreview() {
    E20FrontendMobileTheme {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp).background(white)) {
            TitledBox(title = "Seguaci", content = "123")
        }
    }
}

//@Preview
@Composable
fun UserInfoPreview() {
    E20FrontendMobileTheme {
        UserInfo("Mario", "Rossi", "mario.rossi@gmail.com", "2000-01-01")
    }
}

//@Preview
@Composable
fun LogOutBoxPreview() {
    E20FrontendMobileTheme {
        Box(Modifier.height(300.dp)) {
            LogOutBox()
        }
    }
}