package com.example.e20frontendmobile.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e20frontendmobile.data.apiService.Utente.UtenteService
import com.example.e20frontendmobile.model.Utente
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoggedUserViewModel : ViewModel() {

    private var utenteService: UtenteService? = null

    private fun getService(context: Context): UtenteService {
        if (utenteService == null) utenteService = UtenteService(context.applicationContext)

        return utenteService!!
    }

    // Stato del Logged User
    private val _loggedUser = MutableStateFlow<Utente?>(null)
    val loggedUser: StateFlow<Utente?> = _loggedUser

    fun loadLoggedUser(context: Context) {
        if (_loggedUser.value != null) return

        viewModelScope.launch {
            val currentLoggedUser = getService(context).getLoggedUser()
            Log.d("LoggedUserViewModel", "currentLoggedUser: ${currentLoggedUser}")

            if (currentLoggedUser != null) _loggedUser.value = currentLoggedUser
            Log.d("LoggedUserViewModel", "_loggedUser.value: ${_loggedUser.value}")

        }
    }

    fun logout() { _loggedUser.value = null }

    // Stato dell'Utente Visualizzato
    private val _onScreenUser = MutableStateFlow<Utente?>(null)
    val onScreenUser: StateFlow<Utente?> = _onScreenUser

    fun loadUser(context: Context, username: String) {
        if (_onScreenUser.value?.username == username) return

        viewModelScope.launch {
            val currentUser = getService(context).getUtente(username)
            Log.d("LoggedUserViewModel", "currentUser: ${currentUser}")

            if (currentUser != null) _onScreenUser.value = currentUser
            Log.d("LoggedUserViewModel", "_onScreenUser.value: ${_onScreenUser.value}")

        }
    }


    // Stato della lista Seguaci
    private val _seguaci = MutableStateFlow<List<Utente>>(emptyList())
    val seguaci: StateFlow<List<Utente>> = _seguaci

    private val _isLoadingSeguaci = MutableStateFlow<Boolean>(false)
    val isLoadingSeguaci: StateFlow<Boolean> = _isLoadingSeguaci

    private val _errorSeguaci = MutableStateFlow<String?>(null)
    val errorSeguaci: StateFlow<String?> = _errorSeguaci

    // Stato della lista Seguiti
    private val _seguiti = MutableStateFlow<List<Utente>>(emptyList())
    val seguiti: StateFlow<List<Utente>> = _seguiti

    private val _isLoadingSeguiti = MutableStateFlow<Boolean>(false)
    val isLoadingSeguiti: StateFlow<Boolean> = _isLoadingSeguiti

    private val _errorSeguiti = MutableStateFlow<String?>(null)
    val errorSeguiti: StateFlow<String?> = _errorSeguiti


    fun loadSeguaci(context: Context) {
        viewModelScope.launch {

            _isLoadingSeguaci.value = true
            _errorSeguaci.value = null

            try {
                val result = getService(context).getSeguaciOfLoggedUser()
                Log.d("LoggedUserViewModel", "Seguaci Request: ${result}")

                _seguaci.value = result
            }
            catch (e: Exception) {
                _errorSeguaci.value = "Errore nel caricamento seguaci: ${e.message}"
                Log.d("LoggedUserViewModel", "Seguaci Request: ERRORE")

            }
            finally { _isLoadingSeguaci.value = false }
        }
    }

    fun loadSeguiti(context: Context) {
        viewModelScope.launch {

            _isLoadingSeguiti.value = true
            _errorSeguiti.value = null

            try {
                val result = getService(context).getSeguitiOfLoggedUser()
                Log.d("LoggedUserViewModel", "Seguiti Request: ${result}")

                _seguiti.value = result
            }
            catch (e: Exception) {
                _errorSeguiti.value = "Errore nel caricamento seguiti: ${e.message}"
                Log.d("LoggedUserViewModel", "Seguiti Request: ERRORE")
            }
            finally { _isLoadingSeguiti.value = false }
        }
    }


    // Utils
    fun smettiDiSeguire(username: String) {

    }

}