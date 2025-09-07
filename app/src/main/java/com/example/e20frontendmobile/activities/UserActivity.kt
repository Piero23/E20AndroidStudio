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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e20frontendmobile.composables.IconButtonType1
import com.example.e20frontendmobile.composables.IconTextButtonType1
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme
import com.example.e20frontendmobile.ui.theme.MuseoModerno
import com.example.e20frontendmobile.ui.theme.backgroundGradient
import com.example.e20frontendmobile.ui.theme.spaceExtraSmall
import com.example.e20frontendmobile.ui.theme.spaceLarge
import com.example.e20frontendmobile.ui.theme.spaceMedium

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
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.medium
            )
            .padding(30.dp, 12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.titleLarge,
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
            .background(MaterialTheme.colorScheme.surfaceDim)
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
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)

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

    }
}


// Previews ----------------------------------------------------------------------------------------

@Preview()
@Composable
fun MainScreenPreview() {
    E20FrontendMobileTheme(darkTheme = false) {
        MainScreen(
            username = "username"
        )
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
        TitledBox(title = "Seguaci", content = "123")
    }
}

//@Preview
@Composable
fun UserInfoPreview() {
    E20FrontendMobileTheme {
        UserInfo("Mario", "Rossi", "mario.rossi@gmail.com", "2000-01-01")
    }
}