package com.example.e20frontendmobile.viewModels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.e20frontendmobile.data.apiService.Utente.UtenteService
import com.example.e20frontendmobile.model.UserRegistration
import com.example.e20frontendmobile.model.Utente
import java.time.LocalDate

class RegistrationViewModel : ViewModel() {

    // Internal Variables
    var registratingUserState by mutableStateOf(UserRegistration())
        private set

    // Valuidation Methods
    private fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "Lo Username è obbligatorio."

            username.length < 4 || username.length > 20 ->
                "Lo Username deve contenere almeno 4 caratteri, massimo 20."

            !Regex("^[a-zA-Z0-9._-]+$").matches(username) ->
                "Lo Username può contenere solo lettere, numeri, punti, trattini ed underscore."

            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "La password è obbligatoria."

            password.length < 8 || password.length > 64 ->
                "La password deve contenere almeno 8 caratteri, massimo 64."

            !Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&]).{8,}$")
                .matches(password) ->
                "La password deve contenere almeno una lettera maiuscola, una minuscola, " +
                        "un numero ed un carattere speciale (@\$!%*?&)."

            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "L'indirizzo email è obbligatorio."

            !Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email) ->
                "L'indirizzo email non è valido."

            else -> null
        }
    }

    private fun validateBirthDate(date: LocalDate?): String? {
        return if (date == null) "La data di nascita è obbligatoria." else null
    }

    fun isFormValid(): Boolean {
        return registratingUserState.errors.values.all { it.isEmpty() }
    }


    // onChange Values Methods
    fun onUsernameChange(newValue: String) {
        // Validating new Username
        val error = validateUsername(newValue)

        // Updating new Username
        registratingUserState = registratingUserState.copy(
            username = newValue,
            errors = registratingUserState.errors + ("username" to (error ?: ""))
        )
    }

    fun onPasswordChange(newValue: String) {
        // Validating new Username
        val error = validatePassword(newValue)

        // Updating new Username
        registratingUserState = registratingUserState.copy(
            password = newValue,
            errors = registratingUserState.errors + ("password" to (error ?: ""))
        )
    }

    fun onEmailChange(newValue: String) {
        // Validating new Username
        val error = validateEmail(newValue)

        // Updating new Username
        registratingUserState = registratingUserState.copy(
            email = newValue,
            errors = registratingUserState.errors + ("email" to (error ?: ""))
        )
    }

    fun onBirthDateChange(newValue: LocalDate) {
        // Validating new Username
        val error = validateBirthDate(newValue)

        // Updating new Username
        registratingUserState = registratingUserState.copy(
            birthDate = newValue,
            errors = registratingUserState.errors + ("birthDate" to (error ?: ""))
        )
    }


    fun registerUser(context: Context) {


        UtenteService(context).register(registratingUserState)
    }

}