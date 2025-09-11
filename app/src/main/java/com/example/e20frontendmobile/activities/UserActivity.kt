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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.e20frontendmobile.composables.CustomTextField
import com.example.e20frontendmobile.composables.IconButtonType1
import com.example.e20frontendmobile.composables.IconTextButtonType1
import com.example.e20frontendmobile.model.UserRegistration
import com.example.e20frontendmobile.toJavaLocalDate
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
import com.example.e20frontendmobile.viewModels.RegistrationViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

//TODO redirectare logout a pagina login con navhostcontroller
@Composable
fun MainAccessUserPage(navController: NavHostController) {
//    val navController = rememberNavController()
//    val user = userViewModel.currentUser
//
//    NavHost(
//        navController = navController,
//        startDestination = if (user == null) "login" else "profile"
//    ) {
//        composable("login") {
//            LoginScreen(
//                onLoginSuccess = { loggedUser ->
//                    userViewModel.login(loggedUser)
//                    navController.navigate("profile") {
//                        popUpTo("login") { inclusive = true } // rimuove login dallo stack
//                    }
//                }
//            )
//        }
//
//        composable("profile") {
//            ProfileScreen(
//                user = userViewModel.currentUser!!,
//                onLogout = {
//                    userViewModel.logout()
//                    navController.navigate("login") {
//                        popUpTo("profile") { inclusive = true }
//                    }
//                },
//                onFollowerClick = { followerId ->
//                    navController.navigate("userDetail/$followerId")
//                }
//            )
//        }
//
//        composable(
//            route = "userDetail/{userId}",
//            arguments = listOf(navArgument("userId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val userId = backStackEntry.arguments?.getString("userId")
//            UserDetailScreen(userId = userId ?: "")
//        }
//    }
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
    email: String,
    birthDate: String,
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
private fun UserRegistrationBox(
    userState: UserRegistration,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onBirthDateChange: (LocalDate) -> Unit,
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
                            .toJavaLocalDate()

                        onBirthDateChange(localDate)
                    }
                },
                onDismiss = { setShowDatePicker(false) }
            )
        }

        // Fields ------------------------------
        // Username Field
        RegistrationTextField(
            label = "Username",
            value = userState.username,
            placeholder = "Your Username",
            onValueChange = onUsernameChange,
            isError = userState.errors["username"]?.isNotEmpty() == true,
            errorMessage = userState.errors["username"]
        )

        // Username Field
        RegistrationTextField(
            label = "Password",
            value = userState.password,
            placeholder = "Your Password",
            onValueChange = onPasswordChange,
            isError = userState.errors["password"]?.isNotEmpty() == true,
            errorMessage = userState.errors["password"]
        )

        Spacer(Modifier.height(spaceMedium))

        // User Email
        RegistrationTextField(
            label = "Email",
            value = userState.email,
            placeholder = "Your Email",
            onValueChange = onEmailChange,
            isError = userState.errors["email"]?.isNotEmpty() == true,
            errorMessage = userState.errors["email"]
        )

        Spacer(Modifier.height(spaceSmall))

        // User Birth Date
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RegistrationTextField(
                label = "Birth Date",
                value = userState.birthDate.toString(),
                placeholder = "Your Birth Date",
                onValueChange = { }, // Not Editable
                isError = userState.errors["birthDate"]?.isNotEmpty() == true,
                errorMessage = userState.errors["birthDate"],

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
        UserInfo("mario.rossi@gmail.com", "2000-01-01")

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
fun RegisterScreen(registrationViewModel: RegistrationViewModel = viewModel()) {

    val userState = registrationViewModel.registratingUserState

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

        // User Registration Box
        UserRegistrationBox(
            userState = userState,
            onUsernameChange = { registrationViewModel.onUsernameChange(it) },
            onPasswordChange = { registrationViewModel.onPasswordChange(it) },
            onEmailChange = { registrationViewModel.onEmailChange(it) },
            onBirthDateChange = { registrationViewModel.onBirthDateChange(it) },
        )
    }
}

// Previews ----------------------------------------------------------------------------------------

@Preview
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
        UserInfo("mario.rossi@gmail.com", "2000-01-01")
    }
}
