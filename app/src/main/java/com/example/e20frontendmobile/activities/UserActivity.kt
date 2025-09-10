package com.example.e20frontendmobile.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavHostController
import com.example.e20frontendmobile.composables.CustomTextField
import com.example.e20frontendmobile.composables.IconButtonType1
import com.example.e20frontendmobile.composables.IconTextButtonType1
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme
import com.example.e20frontendmobile.ui.theme.backgroundGradient
import com.example.e20frontendmobile.ui.theme.backgroundLinearGradient
import com.example.e20frontendmobile.ui.theme.blurredDropShadow
import com.example.e20frontendmobile.ui.theme.iconSizeSmall
import com.example.e20frontendmobile.ui.theme.spaceExtraSmall
import com.example.e20frontendmobile.ui.theme.spaceSmall
import com.example.e20frontendmobile.ui.theme.spaceLarge
import com.example.e20frontendmobile.ui.theme.spaceMedium
import com.example.e20frontendmobile.ui.theme.white
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

//TODO redirectare logout a pagina login con navhostcontroller
@Composable
fun MainAccessUserPage(navController: NavHostController) {

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
                offset = Offset(10f, 10f),
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
                offset = Offset(10f, 10f),
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
fun RegistrationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    errorMessage: String? = null,
    isPassword: Boolean = false
) {
    Column(
        modifier = modifier
            .height(100.dp)
            .background(white)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onTertiary,
            modifier = Modifier.padding(start = 10.dp)
        )

        CustomTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            singleLine = true,
            placeholder = placeholder,
            isPassword = isPassword
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}


@OptIn(ExperimentalTime::class)
@Composable
fun UserRegistrationBox(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .blurredDropShadow(
                shadowColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.25f),
                offset = Offset(10f, 10f),
                blurRadius = 10f,
            )
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.medium
            )
            .padding(spaceMedium)

    ) {
        // User Info
        val (username, setUsername) = remember { mutableStateOf("") }
        val (password, setPassword) = remember { mutableStateOf("") }
        val (firstName, setFirstName) = remember { mutableStateOf("") }
        val (lastName, setLastName) = remember { mutableStateOf("") }
        val (email, setEmail) = remember { mutableStateOf("") }

        val (birthDate, setBirthDate) = remember { mutableStateOf<String?>(null) }
        val (showDatePicker, setShowDatePicker) = rememberSaveable { mutableStateOf(false) }

        // On Show Date Picker to Select a Birth Date
        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { millis ->
                    millis?.let {
                        val localDate = Instant
                            .fromEpochMilliseconds(it)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date

                        setBirthDate(localDate.toString())
                    }
                },
                onDismiss = { setShowDatePicker(false) }
            )
        }

        // Fields ------------------------------

        // Title
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spaceMedium)
        ) {
            Text(
                text = "Registrati",

                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Log In Access
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {
            Text(
                text = "Got already an account?",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiary,

                overflow = TextOverflow.Visible,
                modifier = Modifier
                    .weight(1f)
            )

            Spacer(Modifier.width(spaceLarge))

            IconTextButtonType1(
                onClick = {},
                text = "Log In",
                withIcon = true,
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                modifier = Modifier
                    .blurredDropShadow(
                        shadowColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.25f),
                        offset = Offset(10f,10f),
                        blurRadius = 10f,
                    )
            )
        }

        // Username Field
        RegistrationTextField(
            label = "Username",
            value = username,
            placeholder = "Your Username",
            onValueChange = { setUsername(it) },
            isError = false,
        )

        // Username Field
        RegistrationTextField(
            label = "Password",
            value = password,
            placeholder = "Your Password",
            onValueChange = { setPassword(it) },
            isPassword = true,
            isError = false,
        )

        Spacer(Modifier.height(spaceMedium))

        // User First and Last Name
        Row(Modifier.fillMaxWidth()) {
            // User First Name
            RegistrationTextField(
                label = "First Name",
                value = firstName,
                placeholder = "First Name",
                onValueChange = { setFirstName(it) },
                isError = false,

                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(spaceExtraSmall))

            // User Last Name
            RegistrationTextField(
                label = "Last Name",
                value = lastName,
                onValueChange = { setLastName(it) },
                placeholder = "Last Name",
                isError = false,
                errorMessage = "Questa e' una prova",

                modifier = Modifier.weight(1f)
            )
        }

        // User Email
        RegistrationTextField(
            label = "Email",
            value = email,
            placeholder = "Your Email",
            onValueChange = { setEmail(it) },
            isError = (email == "Miao"),
            errorMessage = "Questa e' una prova"
        )

        Spacer(Modifier.height(spaceSmall))

        // User Birth Date
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RegistrationTextField(
                label = "Birth Date",
                value = birthDate ?: "No selected Date" ,
                placeholder = "Your Birth Date",
                onValueChange = {  },
                isError = false,

                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(spaceExtraSmall))

            IconButtonType1(
                onClick = { setShowDatePicker(true) },
                icon = Icons.Default.DateRange,
                iconDescription = "Select your Birth Date",
                iconSize = 22.dp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Spacer(Modifier.height(spaceMedium))

        IconTextButtonType1(
            text = "Registrati",
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun MainProfileScreen(
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
}


@Composable
fun RegisterScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .backgroundLinearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.secondary
                )
            )
            .padding(all = 40.dp)
    ) {
        // User Registration Box
        UserRegistrationBox()
    }
}

// Previews ----------------------------------------------------------------------------------------

//@Preview
@Composable
fun MainScreenPreview() {
    E20FrontendMobileTheme(darkTheme = false) {
        MainProfileScreen(
            username = "username"
        )
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    E20FrontendMobileTheme(darkTheme = false) {
        RegisterScreen()
    }
}

//@Preview
@Composable
fun RegistrationTextFieldPreview() {
    E20FrontendMobileTheme {
        RegistrationTextField(
            value = "",
            onValueChange = { value -> {} },
            label = "Prova di lablel",
            isError = false
        )
    }
}

//@Preview
@Composable
fun RegistrationTextFieldPreview2() {
    E20FrontendMobileTheme {
        RegistrationTextField(
            value = "",
            onValueChange = { value -> {} },
            label = "Prova di lablel",
            isError = true,
            errorMessage = "Questo e' un errore"
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
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .size(200.dp)
            .background(white)) {
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
