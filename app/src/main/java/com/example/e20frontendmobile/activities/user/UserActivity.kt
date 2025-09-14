package com.example.e20frontendmobile.activities

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.e20frontendmobile.activities.evento.DatePickerModal
import com.example.e20frontendmobile.composables.CustomTextField
import com.example.e20frontendmobile.composables.IconButtonType1
import com.example.e20frontendmobile.composables.IconTextButtonType1
import com.example.e20frontendmobile.data.auth.AuthActivity
import com.example.e20frontendmobile.model.UserRegistration
import com.example.e20frontendmobile.model.Utente
import com.example.e20frontendmobile.toJavaLocalDate
import com.example.e20frontendmobile.ui.theme.E20FrontendMobileTheme
import com.example.e20frontendmobile.ui.theme.backgroundGradient
import com.example.e20frontendmobile.ui.theme.backgroundLinearGradient
import com.example.e20frontendmobile.ui.theme.blurredDropShadow
import com.example.e20frontendmobile.ui.theme.overlayBlack10
import com.example.e20frontendmobile.ui.theme.overlayBlack40
import com.example.e20frontendmobile.ui.theme.spaceExtraSmall
import com.example.e20frontendmobile.ui.theme.spaceLarge
import com.example.e20frontendmobile.ui.theme.spaceMedium
import com.example.e20frontendmobile.ui.theme.spaceSmall
import com.example.e20frontendmobile.ui.theme.white
import com.example.e20frontendmobile.viewModels.LoggedUserViewModel
import com.example.e20frontendmobile.viewModels.RegistrationViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun MainAccessUserPage(
    navController: NavHostController,
    loggedUserViewModel: LoggedUserViewModel
) {
    val context = LocalContext.current
    val loggedUser by loggedUserViewModel.loggedUser.collectAsState()
    val onScreenUser by loggedUserViewModel.onScreenUser.collectAsState()

    // 1: Load Logged User Only the first time on the Creation of the Composable
//    LaunchedEffect(Unit) {
//        loggedUserViewModel.loadLoggedUser(context)
//    }

    // 2: AuthActivity Launcher (ActivityResult API)
    val loginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Non navighiamo qui: ricarichiamo lo user e lasciamo che il LaunchedEffect sotto
            // reagisca quando loggedUser cambia.
            Log.d("MainAccessUserPage", "Login in con successo, resultCode = ${result.resultCode}")
            loggedUserViewModel.loadLoggedUser(context)
        } else {
            Log.d("MainAccessUserPage", "Login fallito o annullato")
        }
    }

    // 3: Update Logger User Status on Changes
    LaunchedEffect(loggedUser) {
        Log.d("MainAccessUserPage", "Redirezione perché loggedUser = $loggedUser")

        if (loggedUser != null) {
            // Se siamo loggati, vai al profilo (rimuovi register dallo stack)
            navController.navigate("profile") {
                popUpTo("register") { inclusive = true }
            }
        } else {
            // Se non siamo loggati, assicurati di andare su register
            navController.navigate("register") {
                popUpTo("profile") { inclusive = true }
            }
        }
    }

    // 4: NavHost starting on "register"
    NavHost(
        navController = navController,
        startDestination = "register"
    ) {
        // Waiting for Data
        //composable("loading") { LoadingScreen() }

        composable("register") {
            RegisterScreen(
                onLoginRequest = {
                    // uso il launcher per aprire l'AuthActivity
                    loginLauncher.launch(Intent(context, AuthActivity::class.java))
                }
            )
        }

        composable("profile") {
            // Mostra il profilo solo se abbiamo i dati
            loggedUser?.let { user ->
                MainProfileScreen(
                    currentUser = user,
                    loggedUserViewModel = loggedUserViewModel,
                    onLogoutCallback = {
                        loggedUserViewModel.logout()
                        // logout provoca loggedUser = null -> LaunchedEffect sopra porta a "register"
                    },
                    onAnotherUtenteClickCallback = { username ->
                        navController.navigate("user/$username")
                    }
                )
            } ?: run {
                // se non abbiamo ancora i dati, mostra un loader (meglio di Register)
                LoadingScreen()
            }
        }

        composable(
            route = "user/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""

            // carico l'utente richiesto solo quando cambia il param
            LaunchedEffect(username) {
                loggedUserViewModel.loadUser(context, username)
            }

            when (val user = onScreenUser) {
                null -> LoadingScreen()
                else -> UserInfoProfileScreen(
                    currentUser = user,
                    loggedUserViewModel = loggedUserViewModel
                )
            }
        }
    }
}


// Components --------------------------------------------------------------------------------------

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UserAccordion(
    title: String,
    users: List<Utente>,
    setVisibility: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onTitleClick: () -> Unit = { },
    onUtenteClickCallback: (String) -> Unit = { },
) {
    Box(
        modifier = Modifier
            .zIndex(10f)
            .fillMaxSize()
            .background(overlayBlack40)
            .clickable { onTitleClick() }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
                .background(Color.Transparent)
        ) {

            // Top Rounding
            Box(
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()
                    .height(30.dp)
            ) {}

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = spaceMedium)
                    .background(white)
                    .wrapContentHeight()
            ) {
                // Header cliccabile
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTitleClick() }
                        .padding(spaceMedium)
                ) {
                    Text(text = title, style = MaterialTheme.typography.titleLarge)
                }

               Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(horizontal = spaceMedium, vertical = spaceSmall),
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Spacer(
                       Modifier
                           .height(3.dp)
                           .weight(1f)
                           .background(color = overlayBlack40, shape = MaterialTheme.shapes.small)
                   )
               }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(color = Color.Transparent, shape = MaterialTheme.shapes.large)
                        .padding(start = spaceMedium, end = spaceMedium, bottom = spaceSmall)
                ) {
                    items(users) { user ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = spaceMedium, horizontal = spaceLarge)
                        ) {
                            Text(
                                text = user.username,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(Modifier.weight(1f))

                            IconButtonType1(
                                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                iconDescription = "Mostra di più",
                                onClick = { 
                                    setVisibility(false)
                                    onUtenteClickCallback(user.username) 
                                }
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(spaceSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(
                                Modifier
                                    .height(3.dp)
                                    .weight(1f)
                                    .background(
                                        color = overlayBlack10,
                                        shape = MaterialTheme.shapes.small
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TitledBox(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
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
            .clickable { onClick() }
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
    modifier: Modifier = Modifier,
    modifiable: Boolean = true,
    onModifyUserInfo: () -> Unit = { }
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
        if (modifiable) {
            IconButtonType1(
                onClick = onModifyUserInfo,
                icon = Icons.Default.Edit,
                iconDescription = "",
                iconSize = 20.dp
            )
        }
    }
}

@Composable
fun RegistrationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true,
    placeholder: String? = null,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    readOnly: Boolean = false
) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .defaultMinSize(minHeight = 115.dp)
            .background(Color.Transparent)
    ) {
        if (showLabel) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        CustomTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            singleLine = true,
            placeholder = placeholder,
            isPassword = isPassword,
            readOnly = readOnly
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
            isPassword = true,
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
                label = "Data di Nascita",
                value = if (userState.birthDate == null) "Select a Date" else userState.birthDate.toString(),
                placeholder = "Your Birth Date",
                onValueChange = { },
                isError = userState.errors["birthDate"]?.isNotEmpty() == true,
                errorMessage = userState.errors["birthDate"],
                readOnly = true,

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
    }
}


@Composable
fun MainProfileScreen(
    currentUser: Utente,
    loggedUserViewModel: LoggedUserViewModel,
    onLogoutCallback: () -> Unit,
    onAnotherUtenteClickCallback: (String) -> Unit
    ) {
    val context = LocalContext.current

    // Accordions
    val (showSeguaciAccordion, setShowSeguaciAccordion) = remember { mutableStateOf(false) }
    val (showSeguitiAccordion, setShowSeguitiAccordion) = remember { mutableStateOf(false) }

    // Seguaci Variables
    val seguaci by loggedUserViewModel.seguaci.collectAsState()
    val isLoadingSeguaci by loggedUserViewModel.isLoadingSeguaci.collectAsState()
    val errorSeguaci by loggedUserViewModel.errorSeguaci.collectAsState()

    // Seguiti Variables
    val seguiti by loggedUserViewModel.seguiti.collectAsState()
    val isLoadingSeguiti by loggedUserViewModel.isLoadingSeguiti.collectAsState()
    val errorSeguiti by loggedUserViewModel.errorSeguiti.collectAsState()

    LaunchedEffect(Unit) {
        loggedUserViewModel.loadSeguaci(context)
        loggedUserViewModel.loadSeguiti(context)
    }

    Box {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient())
                .padding(top = 114.dp, start = 40.dp, bottom = 61.dp, end = 40.dp)
        ) {
            // User Circle with Initial
            UserImage(username = currentUser.username)
            Spacer(Modifier.height(spaceExtraSmall))

            // Username
            Text(
                text = "@${currentUser.username}",
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
                TitledBox(
                    title = "Seguaci",
                    content = when {
                        isLoadingSeguaci || !errorSeguaci.isNullOrEmpty() -> "---"
                        else -> "${seguaci.size}"
                    },
                    onClick = { setShowSeguaciAccordion(true) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(spaceMedium))
                TitledBox(
                    title = "Seguiti",
                    content = when {
                        isLoadingSeguiti || !errorSeguiti.isNullOrEmpty()-> "---"
                        else -> "${seguiti.size}"
                    },
                    onClick = { setShowSeguitiAccordion(true) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(spaceMedium))

            // User Info
            UserInfo(currentUser.email, currentUser.dataNascita.toString())

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
                    color = Color.Transparent, //MaterialTheme.colorScheme.onPrimary.copy(alpha = .8f),

                    overflow = TextOverflow.Visible,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 20.dp, 0.dp, 0.dp, 0.dp)
                )

                Spacer(Modifier.width(spaceLarge))

                IconTextButtonType1(
                    onClick = { onLogoutCallback() },
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

    // Seguaci User Accordion
    AnimatedVisibility( visible = showSeguaciAccordion )
    {
        // For Safety
        setShowSeguitiAccordion(false)

        UserAccordion(
            title = "Seguaci",
            users = seguaci,
            onTitleClick = { setShowSeguaciAccordion(false) },
            onUtenteClickCallback = onAnotherUtenteClickCallback,
            setVisibility = setShowSeguaciAccordion
        )
    }

    // Seguiti User Accordion
    AnimatedVisibility( visible = showSeguitiAccordion )
    {
        // For Safety
        setShowSeguaciAccordion(false)

        UserAccordion(
            title = "Seguiti",
            users = seguiti,
            onTitleClick = { setShowSeguitiAccordion(false) },
            onUtenteClickCallback = onAnotherUtenteClickCallback,
            setVisibility = setShowSeguitiAccordion
        )
    }

}

@Composable
fun UserInfoProfileScreen(
    currentUser: Utente,
    loggedUserViewModel: LoggedUserViewModel
) {
    val context = LocalContext.current
    val isCurrentlyFollowing by loggedUserViewModel.isOnScreenUserSeguito.collectAsState()

    Box {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient())
                .padding(top = 114.dp, start = 40.dp, bottom = 61.dp, end = 40.dp)
        ) {
            // User Circle with Initial
            UserImage(username = currentUser.username)
            Spacer(Modifier.height(spaceExtraSmall))

            // Username
            Text(
                text = "@${currentUser.username}",
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
                TitledBox(
                    title = when (isCurrentlyFollowing) {
                        true -> "Smetti di"
                        false -> "Inizia a"
                        null -> "Non puoi"
                    },
                    content = "Seguire",
                    onClick = { loggedUserViewModel.toggleOnScreenUserSeguito(context, currentUser)},
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(spaceMedium))

            // User Info
            UserInfo(
                currentUser.email,
                currentUser.dataNascita.toString(),
                modifiable = false
            )

        }
    }
}


@Composable
fun RegisterScreen(
    onLoginRequest: () -> Unit,
    registrationViewModel: RegistrationViewModel = viewModel()
) {
    val context = LocalContext.current
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
        Column(
            modifier = Modifier
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
                .padding(spaceLarge)
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

            Spacer(Modifier.height(spaceSmall))

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
                    onClick = onLoginRequest,
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

            Spacer(Modifier.height(spaceMedium))

            IconTextButtonType1(
                text = "Registrati",
                onClick = { registrationViewModel.registerUser(context) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Previews ----------------------------------------------------------------------------------------

//@Preview
@Composable
fun MainScreenPreview() {
    E20FrontendMobileTheme(darkTheme = false) {
        val loggedUserViewModel: LoggedUserViewModel = viewModel()
        val loggedUser by loggedUserViewModel.loggedUser.collectAsState()

        MainProfileScreen(
            currentUser = Utente("1", "mario.rossi", "mail@gmail.com", null),
            loggedUserViewModel = loggedUserViewModel,
            onLogoutCallback = { },
            onAnotherUtenteClickCallback = {}
        )
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    E20FrontendMobileTheme(darkTheme = false) {
        val context = LocalContext.current
        RegisterScreen(
            onLoginRequest = {
                val intent = Intent(context, AuthActivity::class.java)
                context.startActivity(intent)
            }
        )
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

//@Preview
@Composable
fun UserAccordionPreview() {
    val users = listOf(
        Utente("1", "mario.rossi", "mail@gmail.com", null),
        Utente("2", "mario.bianchi", "mail@gmail.com", null),
        Utente("3", "mario.verdi", "mail@gmail.com", null),
    )

    UserAccordion(title = "Seguaci", users = users, setVisibility = { })
}
